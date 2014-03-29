package legunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public abstract class MwInterface {
	// protected Logger logger = LoggerFactory.getLogger(getClass());

	public abstract String name();

	public abstract LuaTable getInterface();

	public abstract LuaValue getSetupOptions();

	protected OneArgFunction defaultFunction() {
		return defaultFunction(null);
	}

	protected OneArgFunction defaultFunction(final String argName) {
		return new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue arg) {
				// logger.warn("defaultFunction " + argName + " called");
				return LuaValue.NIL;
			}
		};
	}
}
