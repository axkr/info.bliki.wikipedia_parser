<?php

// convert https://meta.wikimedia.org/wiki/Interwiki_map
// to the Java .properties file format
if ( $argc == 3 ) {
	function quote($value) {
		$quoted = str_replace(':', '\:', $value);
		$quoted = str_replace('=', '\=', $quoted);
		return $quoted;
	}

	$input = $argv[1];
	$output = $argv[2];

	$interwikis = require $input;

	ini_set('date.timezone', 'UTC');

	if( $fp = fopen( $output, 'w' ) ) {
		fputs( $fp, "# Automatically generated from " . $argv[0] . " on " . strftime('%x') . "\n" );
		foreach( $interwikis as $key => $value ) {
			fputs( $fp, quote($key) . '=' . quote($value) . "\n" );
		}
		fclose( $fp );
	} else {
		throw new Exception('Unable to create output file: ' . $output );
	}
} else {
	fwrite(STDERR, $argv[0] . " <input> <output>\n");
	exit( 1 );
}
