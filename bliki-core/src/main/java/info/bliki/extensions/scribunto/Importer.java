package info.bliki.extensions.scribunto;


import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Importer {
    public static final String DEFAULT_DIR = "build/output";

    public static final String TEMPLATE = "Template_";
    public static final String MODULE   = "Module_";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TEXT  = "text";

    private String pageTitle;
    private final File templateDir;
    private final File moduleDir;
    private int importedTemplates, importedModules;

    public Importer(File baseDir) throws IOException {
        templateDir = new File(baseDir, "templates");
        moduleDir = new File(baseDir, "modules");

        createDir(templateDir);
        createDir(moduleDir);
    }

    private void createDir(File dir) throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("could not create dir "+dir);
            }
        }
    }

    private void run() throws XMLStreamException, IOException {
        System.setProperty("javax.xml.parsers.SAXParserFactory",
                "org.apache.xerces.jaxp.SAXParserFactoryImpl");

        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLEventReader eventReader = factory.createFilteredReader(
                factory.createXMLEventReader(System.in),
                new PageFilter());

        while (eventReader.hasNext()) {
            processEvent(eventReader);
        }
    }

    private void processEvent(XMLEventReader eventReader) throws XMLStreamException, IOException {
        XMLEvent event = eventReader.nextEvent();

        if (event.isStartElement()) {
            StartElement startElement = event.asStartElement();
            if (startElement.getName().getLocalPart().equals(TAG_TITLE)) {
                String title = eventReader.getElementText();

                if (title.startsWith(TEMPLATE) || title.startsWith(MODULE)) {
                    pageTitle = title;
                } else {
                    pageTitle = null;
                }
            } else if (startElement.getName().getLocalPart().equals(TAG_TEXT)) {
                if (pageTitle != null) {
                    String text = eventReader.getElementText();
                    save(pageTitle, text);
                }
            }
        }
    }

    private void save(String name, String body) throws IOException {
        if (name.startsWith(TEMPLATE)) {
            saveTemplate(name, body);
        } else if (name.startsWith(MODULE)) {
            saveModule(name, body);
        } else {
            System.err.println("Unknown type:'"+name+"'");
        }
    }

    private void saveTemplate(String name, String body) throws IOException {
        saveFile(getFile(templateDir, name), body);
        importedTemplates += 1;
    }

    private void saveModule(String name, String body) throws IOException {
        saveFile(getFile(moduleDir, name), body);
        importedModules += 1;
    }

    private void saveFile(File file, String body) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(body);
        writer.close();
    }

    private File getFile(File basedir, String name) {
        return new File(basedir, name.replaceAll(File.separator, "_"));
    }

    private String summary() {
        return "Imported " + importedTemplates + " templates, " + importedModules + " modules";
    }

    private static class PageFilter implements EventFilter {
        @Override
        public boolean accept(XMLEvent event) {
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String name = startElement.getName().getLocalPart();
                if (name.equals(TAG_TITLE) || name.equals(TAG_TEXT)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) throws XMLStreamException, IOException {
        File dest;
        if (args.length > 0) {
            dest = new File(args[0]);
        } else {
            dest = new File(DEFAULT_DIR);
        }
        Importer importer = new Importer(dest);
        System.out.println("Importing to directory "+dest);
        importer.run();
        System.out.println(importer.summary());
    }
}
