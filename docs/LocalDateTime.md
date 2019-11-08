# Why LocalDateTime?
* Basically, the Date object is a [redacted] to use so they made it better.
* For documentation purposes, the main reason for choosing the Java 8 Date/Time API is it's Ease of use and Ease of Understanding over the previous Date object.

Examples of Use:
```java
LocalDate localDate = LocalDate.now();
LocalDate.of(2015, 02, 20);
LocalDate.parse("2015-02-20");

LocalDate tomorrow = LocalDate.now().plusDays(1);
localDateTime.minusHours(2);
DayOfWeek sunday = LocalDate.parse("2016-06-12").getDayOfWeek();
boolean leapYear = LocalDate.now().isLeapYear();

localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
String localDateString = localDateTime.format(DateTimeFormatter.ISO_DATE);
```
