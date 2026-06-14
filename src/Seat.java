class Seat {
    String seatNum, seatClass;
    double priceModifier, totalPrice;
    boolean isBooked;

    public Seat(String seatNum, String seatClass, double priceModifier, double totalPrice) {
        this.seatNum = seatNum;
        this.seatClass = seatClass;
        this.priceModifier = priceModifier;
        this.totalPrice = totalPrice;
        this.isBooked = false;
    }
}
