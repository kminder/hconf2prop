/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hconf2prop;

import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Main {

  public static void convert( String arg ) throws IOException {

    File input = new File( arg );
    if( !input.isFile() || !input.canRead() ) {
      throw new IllegalArgumentException( arg );
    }

    Configuration conf = new Configuration();
    conf.addResource( new Path( input.toURI() ) );

    TreeMap<String,String> map = new TreeMap<String,String>();

    Iterator<Map.Entry<String, String>> entries = conf.iterator();
    while ( entries.hasNext() ) {
      Map.Entry<String,String> entry = entries.next();
      map.put( entry.getKey(), entry.getValue() );
    }


    String base = FilenameUtils.getBaseName( input.getName() );
    File output = new File( input.getParentFile(), base + ".prop" );
    FileOutputStream stream = new FileOutputStream( output );
    Writer writer = new OutputStreamWriter( stream );
    BufferedWriter buffer = new BufferedWriter( writer );
    Iterator<String> keys = map.navigableKeySet().iterator();
    while ( keys.hasNext() ) {
      String key = keys.next();
      String value = map.get( key );
      String line = String.format( "%s = %s", key, value );
      buffer.write( line );
      buffer.newLine();
    }
    buffer.close();

  }

  public static void main( String[] args ) throws IOException {
    for ( String arg : args ) {
      convert( arg );
    }
  }

}
