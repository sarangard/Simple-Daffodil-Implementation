package svv.project;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.illinois.ncsa.daffodil.japi.Compiler;
import edu.illinois.ncsa.daffodil.japi.Daffodil;
import edu.illinois.ncsa.daffodil.japi.DataProcessor;
import edu.illinois.ncsa.daffodil.japi.ParseResult;
import edu.illinois.ncsa.daffodil.japi.ProcessorFactory;

public class SchemaValidator 
{
	private static final String TEST_SCHEMA = "csv.dfdl.xsd";
	private static final String TEST_DATA = "SimpleCSV.csv";
	
	public void Execute() throws Exception 
	{
		/**
		 * Create a Daffodil Compiler. Make sure it validates?
		 */
		Compiler c = Daffodil.compiler();
		c.setValidateDFDLSchemas(true);

		/**
		 * Get a URL for the test schema resource. We'll use it later to get the
		 * file or load the XML or whatever...
		 */
		URL schemaUrl = getClass().getResource("/DFDLSchemas/" + TEST_SCHEMA);
		
		/**
		 * And then compile the schema. Why the original vs. the other XML? No
		 * idea...
		 * 
		 * But we get a ProcessorFactory from it.
		 */
		File schemaFiles = new File(schemaUrl.toURI());
		ProcessorFactory pf = c.compileFile(schemaFiles);

		/**
		 * See if there was an error yet?
		 */		
		if (pf.isError()) 
		{
			throw new Exception("error1");
		}

		/**
		 * Get a DataProcessor from the ProcessorFactory. I guess the parameter
		 * tells it where to start processing from? Why XPath here?
		 */
		DataProcessor p = pf.onPath("/");

		/**
		 * Again, check if there is an error with the DataProcessor?
		 */
		if (p.isError()) 
		{
			throw new Exception("error2");
		}

		/**
		 * Get a URL for the test schema resource. We'll use it later to get the
		 * file or load the XML or whatever...
		 */
		URL dataUrl = getClass().getResource("/TestFiles/" + TEST_DATA);
		
		/**
		 * Get a File for the test data.
		 */
		File dataFile = new File(dataUrl.toURI());
		FileInputStream fis = new FileInputStream(dataFile);
		ReadableByteChannel rbc = Channels.newChannel(fis);

		/**
		 * Now finally try to parse the sample data.
		 */
		ParseResult actual = p.parse(rbc);

		/**
		 * And see if there is an error again?
		 */
		if (actual.isError()) 
		{
			throw new Exception("error3");
		}

		Document doc = actual.result();
		XMLOutputter xo = new XMLOutputter();
		xo.setFormat(Format.getPrettyFormat());
		xo.output(doc, System.out);
	}

}