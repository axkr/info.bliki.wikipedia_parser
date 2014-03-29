package legunto.interfaces;

import legunto.Importer;
import legunto.template.Frame;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.*;

public class MwCommon extends MwInterface {
	private final Globals globals;
	private final File[] searchPath;
	private Frame currentFrame;

	private static final MwInterface[] INTERFACES = { new MwSite(),
			new MwUstring(), new MwTitle(),
	// "mw.uri",
	// "mw.language",
	// "mw.message",
	// "mw.text",
	// "mw.html"
	};

	public MwCommon(Globals globals, File... searchPath) throws IOException {
		this.globals = globals;
		this.searchPath = searchPath;
		load();
	}

	@Override
	public String name() {
		return "mw";
	}

	public String execute(String module, String method, Frame frame)
			throws IOException {
		if (!module.startsWith(Importer.MODULE)) {
			module = Importer.MODULE + module;
		}
		LuaValue chunk = globals.load(findModule(module), module, "t", globals)
				.call();
		LuaValue luaFunction = chunk.get(method);
		LuaValue executeFunction = globals.get("mw").get("executeFunction");

		try {
			currentFrame = frame;
			return executeFunction.call(luaFunction).tojstring();
		} finally {
			currentFrame = null;
		}
	}

	private void load() throws IOException {
		load(this);
		for (MwInterface iface : INTERFACES) {
			load(iface);
		}
	}

	private void load(MwInterface luaInterface) throws IOException {
		// logger.debug("loading interface " + luaInterface);
		InputStream is = findModule(luaInterface.name());
		globals.set("mw_interface", luaInterface.getInterface());
		LuaValue pkg = globals.load(is, luaInterface.name(), "t", globals)
				.call();
		LuaValue setupInterface = pkg.get("setupInterface");

		if (!setupInterface.isnil()) {
			setupInterface.call(luaInterface.getSetupOptions());
		}
	}

	@Override
	public LuaTable getInterface() {
		LuaTable table = new LuaTable();
		table.set("loadPackage", load_package());
		table.set("frameExists", frameExists());
		table.set("newChildFrame", newChildFrame());
		table.set("getExpandedArgument", getExpandedArgument());
		table.set("getAllExpandedArguments", getAllExpandedArguments());
		table.set("getFrameTitle", getFrameTitle());
		table.set("expandTemplate", defaultFunction());
		table.set("callParserFunction", defaultFunction());
		table.set("preprocess", defaultFunction());
		table.set("incrementExpensiveFunctionCount", defaultFunction());
		table.set("isSubstring", defaultFunction());
		return table;
	}

	private LuaValue getExpandedArgument() {
		return new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue frameId, LuaValue name) {
				// logger.debug("getExpandedArgument(" + frameId + "," + name +
				// ")");
				Frame frame;
				if (frameId.tojstring().equals("parent")) {
					frame = currentFrame.getParent();
				} else {
					frame = currentFrame;
				}

				return frame.getArgument(name.tojstring());
			}
		};
	}

	private LuaValue getFrameTitle() {
		return new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue arg) {
				// logger.debug("getFrameTitle");
				return NIL;
			}
		};
	}

	private LuaValue getAllExpandedArguments() {
		return new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue arg) {
				// logger.debug("getAllExpandedArguments");
				return NIL;
			}
		};
	}

	private LuaValue newChildFrame() {
		return new ThreeArgFunction() {
			@Override
			public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
				// logger.debug("newChildFrame");
				return LuaValue.NIL;
			}
		};
	}

	private LuaValue frameExists() {
		return new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue arg) {
				// logger.debug("frameExists(" + arg + ")");
				return LuaValue.TRUE;
			}
		};
	}

	private OneArgFunction load_package() {
		return new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue arg) {
				String name = arg.tojstring();
				// logger.debug("loadPackage: " + name);
				try {
					InputStream is = findModule(name);
					if (is != null) {
						return globals.load(is, name, "t", globals);
					}
				} catch (IOException ignored) {
					// logger.error("error loading file", ignored);
				}
				return LuaValue.NIL;
			}
		};
	}

	private InputStream findModule(String name) throws IOException {
		name = name.replaceAll("/", "_");
		File file = null;
		for (File path : searchPath) {
			File f = new File(path, name);
			if (f.isFile()) {
				file = f;
				break;
			} else {
				f = new File(f.getAbsolutePath() + ".lua");
				if (f.isFile()) {
					file = f;
					break;
				}
			}
		}
		if (file != null && file.canRead() && file.length() > 0) {
			// TestHelper.copyModule(file);
			return new FileInputStream(file);
		} else {
			throw new FileNotFoundException("could not find module " + name);
		}
	}

	@Override
	public LuaValue getSetupOptions() {
		return new LuaTable();
	}
}
