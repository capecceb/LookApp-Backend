package lookapp.backend

class DateTimeParserTest extends GroovyTestCase {
    void testParseSearchFormat() {
        when: 'I try to parse search date format'
        String str = "1986-04-08T00:00";
        Date date=DateTimeParser.parseSearchFormat(str)

        then: 'The result is ...'
        assert date.toString() == "Tue Apr 08 00:00:00 ART 1986"
    }

    void testParse() {
        when: 'I try to parse json ISO_INSTANT format'
        String str = "2019-10-12T17:00:00Z";
        Date date=DateTimeParser.parse(str)

        then: 'The result is ...'
        assert date.toString() == "Sat Oct 12 14:00:00 ART 2019"
    }
}
