CREATE_SCRIPT = src/main/php/create_interwiki_properties.php
INTERWIKI_PHP_URL = https://noc.wikimedia.org/conf/interwiki.php.txt

src/main/resources/interwiki.properties: interwiki.php $(CREATE_SCRIPT)
	php $(CREATE_SCRIPT) $< $@

interwiki.php:
	curl --silent $(INTERWIKI_PHP_URL) -o $@
