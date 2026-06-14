import java.util.ArrayList;
import java.util.List;

class MockDatabase {
    static List<User> users   = new ArrayList<>();
    static List<Flight> flights = new ArrayList<>();

    static void init() {
        users.add(new User("Aryan Talan", "Passenger"));

        // Flights from New Delhi to around the world
        flights.add(new Flight("FL104", "Lufthansa",          "New Delhi", "Nepal",                 9653.00,  "1h 45m",  0, "06:00", "07:45"));
        flights.add(new Flight("FL105", "AeroLines",          "New Delhi", "Oman",                 12519.00,  "4h 20m",  0, "08:30", "12:50"));
        flights.add(new Flight("FL106", "IndiGo",             "New Delhi", "Kyrgyzstan",           15505.00,  "5h 10m",  1, "10:00", "16:10"));
        flights.add(new Flight("FL107", "SpiceJet",           "New Delhi", "Bangladesh",           15521.00,  "2h 05m",  0, "11:45", "13:50"));
        flights.add(new Flight("FL108", "Air India",          "New Delhi", "Thailand",             16985.00,  "4h 55m",  1, "14:00", "19:55"));
        flights.add(new Flight("FL109", "Etihad",             "New Delhi", "Maldives",             17133.00,  "3h 30m",  0, "15:20", "18:50"));
        flights.add(new Flight("FL110", "Lufthansa",          "New Delhi", "Sri Lanka",            18376.00,  "3h 15m",  0, "17:00", "20:15"));
        flights.add(new Flight("FL111", "Emirates",           "New Delhi", "United Arab Emirates", 18454.00,  "3h 45m",  0, "20:30", "00:15"));
        flights.add(new Flight("FL112", "Singapore Airlines", "New Delhi", "Singapore",            24500.00,  "5h 55m",  0, "21:55", "06:10"));
        flights.add(new Flight("FL113", "Malaysia Airlines",  "New Delhi", "Malaysia",             21200.00,  "5h 30m",  0, "23:00", "07:00"));
        flights.add(new Flight("FL114", "Thai Airways",       "New Delhi", "Thailand",             18500.00,  "4h 20m",  0, "11:40", "17:30"));
        flights.add(new Flight("FL115", "VietJet Air",        "New Delhi", "Vietnam",              16400.00,  "5h 00m",  0, "23:50", "06:20"));
        flights.add(new Flight("FL116", "Qatar Airways",      "New Delhi", "Qatar",                28900.00,  "4h 15m",  0, "10:00", "12:45"));
        flights.add(new Flight("FL117", "Gulf Air",           "New Delhi", "Bahrain",              22350.00,  "4h 40m",  0, "05:50", "09:00"));
        flights.add(new Flight("FL118", "Saudi",              "New Delhi", "Saudi Arabia",         26800.00,  "5h 15m",  0, "19:10", "22:55"));
        flights.add(new Flight("FL119", "Oman Air",           "New Delhi", "Oman",                 19400.00,  "3h 50m",  0, "16:15", "18:35"));
        flights.add(new Flight("FL120", "Kuwait Airways",     "New Delhi", "Kuwait",               24100.00,  "4h 55m",  0, "06:45", "10:10"));
        flights.add(new Flight("FL121", "British Airways",    "New Delhi", "United Kingdom",       62400.00,  "9h 15m",  0, "11:05", "15:50"));
        flights.add(new Flight("FL122", "Virgin Atlantic",    "New Delhi", "United Kingdom",       59800.00,  "9h 25m",  0, "14:15", "19:10"));
        flights.add(new Flight("FL123", "Air France",         "New Delhi", "France",               65200.00,  "9h 35m",  0, "00:35", "06:40"));
        flights.add(new Flight("FL124", "KLM",                "New Delhi", "Netherlands",          67900.00,  "9h 40m",  0, "03:25", "09:35"));
        flights.add(new Flight("FL125", "Lufthansa",          "New Delhi", "Germany",              63100.00,  "8h 50m",  0, "02:25", "06:45"));
        flights.add(new Flight("FL126", "Turkish Airlines",   "New Delhi", "Turkey",               48500.00,  "7h 15m",  0, "06:55", "11:40"));
        flights.add(new Flight("FL127", "LOT Polish Airlines","New Delhi", "Poland",               54200.00,  "8h 20m",  0, "08:15", "13:05"));
        flights.add(new Flight("FL128", "Air India",          "New Delhi", "United States",        98500.00, "15h 40m",  0, "02:20", "07:30"));
        flights.add(new Flight("FL129", "United Airlines",    "New Delhi", "United States",       105400.00, "16h 05m",  0, "23:35", "05:10"));
        flights.add(new Flight("FL130", "Air India",          "New Delhi", "Canada",              112000.00, "15h 10m",  0, "03:10", "07:50"));
        flights.add(new Flight("FL131", "Japan Airlines",     "New Delhi", "Japan",                68700.00,  "7h 55m",  0, "19:05", "06:30"));
        flights.add(new Flight("FL132", "ANA",                "New Delhi", "Japan",                71200.00,  "8h 10m",  0, "01:25", "13:05"));
        flights.add(new Flight("FL133", "Asiana Airlines",    "New Delhi", "South Korea",          53400.00,  "7h 25m",  0, "23:50", "09:45"));
        flights.add(new Flight("FL134", "Cathay Pacific",     "New Delhi", "Hong Kong",            42100.00,  "5h 40m",  0, "01:10", "09:20"));
        flights.add(new Flight("FL135", "Air India",          "New Delhi", "Australia",            76300.00, "12h 25m",  1, "13:45", "05:40"));
        flights.add(new Flight("FL136", "Qantas",             "New Delhi", "Australia",            81400.00, "10h 30m",  0, "18:15", "08:15"));
        flights.add(new Flight("FL137", "Air Astana",         "New Delhi", "Kazakhstan",           23500.00,  "4h 10m",  0, "12:20", "17:00"));
        flights.add(new Flight("FL138", "Uzbekistan Airways", "New Delhi", "Uzbekistan",           19800.00,  "3h 20m",  0, "08:40", "11:30"));
        flights.add(new Flight("FL139", "EgyptAir",           "New Delhi", "Egypt",                44200.00,  "6h 45m",  0, "02:45", "07:00"));
        flights.add(new Flight("FL140", "Ethiopian Airlines", "New Delhi", "Ethiopia",             41900.00,  "7h 10m",  0, "09:30", "14:10"));
        flights.add(new Flight("FL141", "Air Mauritius",      "New Delhi", "Mauritius",            49500.00,  "7h 30m",  0, "04:15", "10:15"));
        flights.add(new Flight("FL142", "SriLankan Airlines", "New Delhi", "Sri Lanka",            21400.00,  "3h 35m",  0, "18:35", "22:10"));
    }
}
