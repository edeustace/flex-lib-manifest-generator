/**
 *  Copyright (c) 2007 - 2009 Adobe
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 */
package com.adobe.ac.manifestGenerator.groovy

/**
 * Created by IntelliJ IDEA.
 * User: eeustace
 * Date: 29-Oct-2009
 * Time: 10:45:45
 * To change this template use File | Settings | File Templates.
 */
class ManifestGeneratorTest extends GroovyTestCase {

	void testGenerateWorks()
	{
		assertTrue true
		ManifestGenerator generator = new ManifestGenerator()
		URL url = this.getClass().getResource(".")
		generator.generate url.getPath() + "DummyProject1/flex_src/", url.getPath() + "DummyProject1/flex_src/manifest.xml"
	}

	void testGenerateWorksForIdenticalClassNames()
	{
		ManifestGenerator generator = new ManifestGenerator()
		URL url = this.getClass().getResource(".")
	  	boolean errorCaught = false
	  	String manifestPath = url.getPath() + "DummyProjectWithSameClassName/manifest2.xml"

	  	try
		{
		  generator.generate( url.getPath() + "DummyProjectWithSameClassName/flex_src/", manifestPath )

		}
		catch( RuntimeException e )
		{
			errorCaught = true
		}
		assertTrue( "The same name error was caught", errorCaught )
	}
}
