class Flight {
    String id, airline, origin, dest, duration, depTime, arrTime;
    double basePrice;
    int stops;

    public Flight(String id, String airline, String origin, String dest,
                  double basePrice, String duration, int stops, String depTime, String arrTime) {
        this.id = id;
        this.airline = airline;
        this.origin = origin;
        this.dest = dest;
        this.basePrice = basePrice;
        this.duration = duration;
        this.stops = stops;
        this.depTime = depTime;
        this.arrTime = arrTime;
    }
}
