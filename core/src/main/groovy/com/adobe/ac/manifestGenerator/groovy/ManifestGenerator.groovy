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
 * Time: 10:51:36
 * To change this template use File | Settings | File Templates.
 */
class ManifestGenerator
{
	List<String> classList	= new ArrayList<String>()
	String sourcePath
	String manifestStart = "<componentPackage>"
	String manifestEnd = "</componentPackage>"
	int AS_SUFFIX_COUNT = 3
	int MXML_SUFFIX_COUNT = 5


	def generate(path, manifestPath)
	{
		sourcePath = path

		new File(path).eachFileRecurse
		{
			f -> addClassToList(f, classList)
		}

		String xmlOut = manifestStart + "\n"
		classList.each{ c -> xmlOut+= "\t<component id=\""+ createId(xmlOut,c) +"\" class=\""+ c +"\"/>\n" }
		xmlOut += manifestEnd

		File outFile = new File( manifestPath )
		outFile.write	xmlOut
	}

	private def addClassToList( File f, List<String> classList )
	{
		if( isAsOrMxml(f) )
 		{
			classList.add( createFullyQualifiedClassName(f.getPath(), sourcePath) )
		}
	}

	private def isAsOrMxml( File f ) { f.getPath().endsWith(".mxml") || f.getPath().endsWith(".as") }

	private def createFullyQualifiedClassName( String filePath , String sourcePath )
	{
		String slashesCorrectedPath = filePath.replace( "\\", "/")
		String pathToFind = sourcePath

		if( filePath.startsWith("/"))
		{
			pathToFind = sourcePath.substring( 1 )
		}

		int index = slashesCorrectedPath.indexOf( filePath ) + 1
		String trimmed = slashesCorrectedPath.substring( index + pathToFind.length() )
		return stripSuffix( trimmed ).replace("/",".")
	}

	private def stripSuffix( String path )
	{
			int count = path.endsWith(".as") ? AS_SUFFIX_COUNT : MXML_SUFFIX_COUNT
			path.substring( 0, path.length() - count )
	}

	private def createClassName( String filePath )
	{
		String stripped = filePath.replace( sourcePath, "" )
		return stripped
	}

	private def createId( String existingManifest, String className ) 
	{
		getFirstAvailableId( existingManifest, className, 0 )
	}

	private def getFirstAvailableId( String existingManifest , String path , int packageLevel ) 
	{
	  	String proposedId = getClassName( path, packageLevel )

	  	if( !idAvailable( existingManifest, proposedId))
	 	{
		  throw new RuntimeException("Error: You already have a class named: " + proposedId + " you should rename the class.");
		}
	  	proposedId
	  //idAvailable( existingManifest, proposedId ) ? proposedId : getFirstAvailableId( existingManifest, path, packageLevel + 1 )
	}

	private def idAvailable( String manifest , String id )
	{
		!manifest.contains( "id=\""+id+"\"")
	}

	private def getClassName( String className, int packageLevel )
	{
		String[] packageArray = className.split("\\.")
		List<String> packageList = packageArray as List

		if( packageLevel > packageArray.length )
		{
			throw new RuntimeException("can't create id for classname: " + className + " and for package level: "	+ packageLevel )
		}

		if( packageArray.length == 0 )
		{
			return className
		}

		int index = packageList.size() - 1 - packageLevel;
		String outString = ""
		outString = packageList.subList( index, packageArray.length ).join("_")
		outString
	}
}
