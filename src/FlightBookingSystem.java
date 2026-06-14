import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class FlightBookingSystem extends JFrame {

    // --- UI Theme Constants ---
    public static final Color PURE_WHITE      = new Color(255, 255, 255);
    public static final Color SKY_BLUE        = new Color(235, 244, 250);
    public static final Color MID_TONE_BLUE_A = new Color(74, 144, 226, 40);
    public static final Color NAVY_BLUE       = new Color(26, 54, 93);
    public static final Color ELECTRIC_BLUE   = new Color(0, 122, 255);
    public static final Color SLATE_GRAY      = new Color(74, 85, 104);
    public static final Color SUCCESS_GREEN   = new Color(34, 197, 94);

    // Class Colors
    public static final Color FIRST_CLASS_COLOR = new Color(255, 215, 0);
    public static final Color BUSINESS_COLOR    = new Color(138, 43, 226);
    public static final Color ECONOMY_COLOR     = new Color(173, 216, 230);

    // --- State & Navigation ---
    private CardLayout cardLayout;
    private JPanel     mainContainer;
    private Flight     selectedFlight;
    private Seat       selectedSeat;
    private User       currentUser;
    private String     generatedPNR;

    // View references
    private SearchView        searchView;
    private SeatSelectionView seatSelectionView;
    private CheckoutView      checkoutView;
    private TicketView        ticketView;

    public FlightBookingSystem() {
        setTitle("TalanAir – Modern Flight Booking");
        setSize(1200, 820);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MockDatabase.init();
        currentUser = MockDatabase.users.get(0);

        cardLayout    = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        VectorBackgroundPanel bgPanel = new VectorBackgroundPanel();
        bgPanel.setLayout(new BorderLayout());

        // Initialize views
        searchView        = new SearchView();
        seatSelectionView = new SeatSelectionView();
        checkoutView      = new CheckoutView();
        ticketView        = new TicketView();

        mainContainer.add(new DashboardView(),  "Dashboard");
        mainContainer.add(searchView,           "Search");
        mainContainer.add(seatSelectionView,    "Seats");
        mainContainer.add(checkoutView,         "Checkout");
        mainContainer.add(ticketView,           "Ticket");

        bgPanel.add(mainContainer, BorderLayout.CENTER);
        bgPanel.add(createFooter(),  BorderLayout.SOUTH);

        add(bgPanel);
        cardLayout.show(mainContainer, "Dashboard");
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new FlightBookingSystem().setVisible(true));
    }

    // ==========================================
    // ROUTING HELPER
    // ==========================================
    public void openSearchForDestination(String destination) {
        searchView.performSearch(destination);
        cardLayout.show(mainContainer, "Search");
    }

    // ==========================================
    // FOOTER
    // ==========================================

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(NAVY_BLUE);
        footer.setBorder(new EmptyBorder(10, 30, 10, 30));
        footer.setPreferredSize(new Dimension(getWidth(), 44));

        JLabel left = new JLabel("✈  TalanAir™  |  Talan Private Limited®");
        left.setForeground(new Color(180, 200, 230));
        left.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel right = new JLabel("© " + LocalDate.now().getYear()
                + " Talan Private Limited. All rights reserved.");
        right.setForeground(new Color(140, 165, 200));
        right.setFont(new Font("SansSerif", Font.PLAIN, 11));
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        footer.add(left,  BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // ==========================================
    // SHARED UI HELPERS
    // ==========================================

    class VectorBackgroundPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, PURE_WHITE, getWidth(), getHeight(), SKY_BLUE);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(MID_TONE_BLUE_A);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.draw(new CubicCurve2D.Double(-100, 200, 300, -100, 600, 500, getWidth() + 100, 100));
            g2d.draw(new CubicCurve2D.Double(-100, 500, 400, 800, 700, 200, getWidth() + 100, 600));
        }
    }

    class GlassCardPanel extends JPanel {
        public GlassCardPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 15));
            g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);
            g2d.setColor(PURE_WHITE);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 5, getHeight() - 5, 16, 16));
            super.paintComponent(g);
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isRollover() ? ELECTRIC_BLUE.darker() : ELECTRIC_BLUE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(PURE_WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    private JButton createOutlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isRollover() ? new Color(0, 122, 255, 20) : new Color(0,0,0,0));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(ELECTRIC_BLUE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(ELECTRIC_BLUE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 26));
        label.setForeground(NAVY_BLUE);
        return label;
    }

    // ==========================================
    // CUSTOM DESTINATION CARD COMPONENT
    // ==========================================
    class DestinationCard extends JPanel {
        private String destinationName;
        private String searchQuery;
        private String priceHint;
        private Image bgImage;

        private float scale = 1.0f;
        private float targetScale = 1.0f;
        private Timer animTimer;

        public DestinationCard(String dest, String searchQuery, String priceHint, String imagePath) {
            this.destinationName = dest;
            this.searchQuery = searchQuery;
            this.priceHint = priceHint;

            // Load the image from your local system
            try {
                bgImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                System.err.println("Could not load image for " + dest + ": " + imagePath);
            }

            setPreferredSize(new Dimension(240, 320));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            animTimer = new Timer(15, e -> {
                scale += (targetScale - scale) * 0.2f;
                if (Math.abs(targetScale - scale) < 0.005f) {
                    scale = targetScale;
                    animTimer.stop();
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    targetScale = 1.06f;
                    animTimer.start();
                }
                @Override public void mouseExited(MouseEvent e) {
                    targetScale = 1.0f;
                    animTimer.start();
                }
                @Override public void mouseClicked(MouseEvent e) {
                    openSearchForDestination(searchQuery);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Call super first so Swing clears dirty pixels — prevents scroll ghosting.
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int shadowOffset = 4;
            int cardX      = shadowOffset;
            int cardY      = shadowOffset;
            int cardWidth  = getWidth()  - shadowOffset * 2;
            int cardHeight = getHeight() - shadowOffset * 2;
            int arc        = 24;

            // --- Drop shadow (fixed, never scales) ---
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillRoundRect(cardX + 4, cardY + 4, cardWidth, cardHeight, arc, arc);

            // --- Card shape (fixed rounded rect — this never moves) ---
            Shape cardShape = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, arc, arc);

            // Clip strictly to the card shape so the image can't bleed outside the corners.
            Shape originalClip = g2d.getClip();
            g2d.clip(cardShape);

            // --- Background image — zoomed by expanding draw rect around center ---
            if (bgImage != null) {
                int imgW = bgImage.getWidth(null);
                int imgH = bgImage.getHeight(null);

                // Base scale to fill the card
                double baseScale = Math.max(
                        (double) cardWidth  / imgW,
                        (double) cardHeight / imgH
                );
                // Apply the hover zoom on top of the base scale
                double totalScale = baseScale * scale;

                int drawW = (int) (imgW * totalScale);
                int drawH = (int) (imgH * totalScale);

                // Centre the (possibly zoomed) image within the card
                int imgX = cardX + (cardWidth  - drawW) / 2;
                int imgY = cardY + (cardHeight - drawH) / 2;

                g2d.drawImage(bgImage, imgX, imgY, drawW, drawH, null);
            } else {
                g2d.setColor(SLATE_GRAY);
                g2d.fill(cardShape);
            }

            // --- Gradient overlay (clipped inside card) ---
            GradientPaint overlay = new GradientPaint(
                    0, cardY + cardHeight / 2.0f, new Color(0, 0, 0, 0),
                    0, cardY + cardHeight,         new Color(0, 0, 0, 200)
            );
            g2d.setPaint(overlay);
            g2d.fill(cardShape);

            // Restore clip so text isn't cut off
            g2d.setClip(originalClip);

            // --- Text (always at fixed positions, never scales) ---
            g2d.setColor(PURE_WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 28));
            g2d.drawString(destinationName, cardX + 16, getHeight() - 76);

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2d.drawString("Flights from " + priceHint, cardX + 16, getHeight() - 50);

            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
            g2d.drawString("➔", getWidth() - 45, getHeight() - 50);

            g2d.dispose();
        }
    }
    // ==========================================
    // DASHBOARD VIEW
    // ==========================================

    class DashboardView extends JPanel {
        public DashboardView() {
            setOpaque(false);
            setLayout(new BorderLayout(0, 10));
            setBorder(new EmptyBorder(30, 40, 20, 40));

            // --- Top Hero Banner ---
            GlassCardPanel heroCard = new GlassCardPanel();
            heroCard.setLayout(new BorderLayout(20, 20));
            heroCard.setPreferredSize(new Dimension(0, 180));

            JPanel heroLeft = new JPanel();
            heroLeft.setLayout(new BoxLayout(heroLeft, BoxLayout.Y_AXIS));
            heroLeft.setOpaque(false);

            JLabel logo = new JLabel("✈  TalanAir");
            logo.setFont(new Font("SansSerif", Font.BOLD, 36));
            logo.setForeground(ELECTRIC_BLUE);

            JLabel title = createHeaderLabel("Welcome back, " + currentUser.name + "!");
            title.setBorder(new EmptyBorder(10, 0, 5, 0));

            JLabel tierLbl = new JLabel("Tier: " + currentUser.getTier() + "  |  Miles: " + currentUser.miles);
            tierLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            tierLbl.setForeground(SLATE_GRAY);

            heroLeft.add(logo);
            heroLeft.add(title);
            heroLeft.add(tierLbl);

            JPanel heroRight = new JPanel(new GridBagLayout());
            heroRight.setOpaque(false);
            JButton searchAllBtn = createStyledButton("Search All Flights");
            searchAllBtn.setPreferredSize(new Dimension(220, 50));
            searchAllBtn.addActionListener(e -> {
                searchView.performSearch("");
                cardLayout.show(mainContainer, "Search");
            });
            heroRight.add(searchAllBtn);

            heroCard.add(heroLeft, BorderLayout.WEST);
            heroCard.add(heroRight, BorderLayout.EAST);

            // --- Bottom Carousel Section ---
            JPanel carouselSection = new JPanel(new BorderLayout(0, 15));
            carouselSection.setOpaque(false);
            carouselSection.setBorder(new EmptyBorder(20, 0, 0, 0));

            JLabel sectionTitle = createHeaderLabel("Explore Popular Destinations");
            carouselSection.add(sectionTitle, BorderLayout.NORTH);

            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
            cardsPanel.setOpaque(false);

            // Add Destination Cards (Replace the paths with the actual locations on your computer)
            cardsPanel.add(new DestinationCard("Japan",     "Japan",                "₹68K", "img/japan.jpeg"));
            cardsPanel.add(new DestinationCard("France",    "France",               "₹65K", "img/france.jpeg"));
            cardsPanel.add(new DestinationCard("UK",        "United Kingdom",       "₹59K", "img/uk.jpeg"));
            cardsPanel.add(new DestinationCard("Maldives",  "Maldives",             "₹17K", "img/maldives.jpg"));
            cardsPanel.add(new DestinationCard("UAE",       "United Arab Emirates", "₹18K", "img/uae.jpeg"));
            cardsPanel.add(new DestinationCard("Australia", "Australia",            "₹76K", "img/australia.jpg"));

            // Horizontal Scroll Pane for Cards
            JScrollPane scrollPane = new JScrollPane(cardsPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            // Speed up scroll
            scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

            // Custom invisible scrollbar UI
            scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

            carouselSection.add(scrollPane, BorderLayout.CENTER);

            add(heroCard, BorderLayout.NORTH);
            add(carouselSection, BorderLayout.CENTER);
        }
    }

    // ==========================================
    // SEARCH VIEW
    // ==========================================

    class SearchView extends JPanel {

        private JTextField searchField;
        private JPanel     listPanel;

        public SearchView() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            GlassCardPanel card = new GlassCardPanel();
            card.setLayout(new BorderLayout(15, 15));
            card.setPreferredSize(new Dimension(960, 580));

            // --- Header row ---
            JPanel topRow = new JPanel(new BorderLayout(10, 0));
            topRow.setOpaque(false);
            topRow.add(createHeaderLabel("Available Flights – New Delhi"), BorderLayout.WEST);

            searchField = new JTextField();
            searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
            searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 215, 230), 1, true),
                    new EmptyBorder(6, 10, 6, 10)));
            searchField.setPreferredSize(new Dimension(220, 36));
            searchField.putClientProperty("JTextField.placeholderText", "Filter by destination or airline…");
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterFlights(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterFlights(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { filterFlights(); }
            });

            JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            searchWrap.setOpaque(false);
            searchWrap.add(new JLabel("🔍  "));
            searchWrap.add(searchField);
            topRow.add(searchWrap, BorderLayout.EAST);

            card.add(topRow, BorderLayout.NORTH);

            JPanel centerArea = new JPanel(new BorderLayout(0, 6));
            centerArea.setOpaque(false);
            centerArea.add(buildFlightRowHeader(), BorderLayout.NORTH);

            listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setOpaque(false);
            listPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

            populateFlights(MockDatabase.flights);

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            centerArea.add(scroll, BorderLayout.CENTER);
            card.add(centerArea, BorderLayout.CENTER);

            // --- Bottom bar ---
            JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            bottomBar.setOpaque(false);

            JButton backBtn = createOutlineButton("← Back");
            backBtn.setPreferredSize(new Dimension(120, 38));
            backBtn.addActionListener(e -> cardLayout.show(mainContainer, "Dashboard"));
            bottomBar.add(backBtn);

            JLabel hint = new JLabel("  Click a flight to choose your seat");
            hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
            hint.setForeground(SLATE_GRAY);
            bottomBar.add(hint);

            card.add(bottomBar, BorderLayout.SOUTH);
            add(card);
        }

        // Programmatic method to force a search from the Dashboard
        public void performSearch(String query) {
            searchField.setText(query);
            filterFlights();
        }

        private JPanel buildFlightRowHeader() {
            JPanel h = new JPanel(null);
            h.setOpaque(false);
            h.setPreferredSize(new Dimension(900, 28));

            String[] cols  = {"Flight", "Airline", "Destination", "Duration", "Stops", "Base Fare"};
            int[]    xPos  = { 10,       90,        210,           430,        560,     670 };

            for (int i = 0; i < cols.length; i++) {
                JLabel lbl = new JLabel(cols[i]);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                lbl.setForeground(new Color(140, 155, 175));
                lbl.setBounds(xPos[i], 4, 160, 20);
                h.add(lbl);
            }
            JSeparator sep = new JSeparator();
            sep.setBounds(0, 26, 900, 1);
            sep.setForeground(new Color(215, 225, 240));
            h.add(sep);
            return h;
        }

        private void populateFlights(List<Flight> flights) {
            listPanel.removeAll();
            if (flights.isEmpty()) {
                JLabel none = new JLabel("No flights match your search.");
                none.setForeground(SLATE_GRAY);
                none.setFont(new Font("SansSerif", Font.ITALIC, 14));
                none.setBorder(new EmptyBorder(20, 10, 20, 10));
                listPanel.add(none);
            }
            for (Flight f : flights) {
                listPanel.add(buildFlightRow(f));
                listPanel.add(Box.createVerticalStrut(6));
            }
            listPanel.revalidate();
            listPanel.repaint();
        }

        private void filterFlights() {
            String q = searchField.getText().trim().toLowerCase();
            if (q.isEmpty()) { populateFlights(MockDatabase.flights); return; }
            List<Flight> filtered = new ArrayList<>();
            for (Flight f : MockDatabase.flights) {
                if (f.dest.toLowerCase().contains(q) || f.airline.toLowerCase().contains(q)
                        || f.id.toLowerCase().contains(q)) {
                    filtered.add(f);
                }
            }
            populateFlights(filtered);
        }

        private JPanel buildFlightRow(Flight f) {
            JPanel row = new JPanel(null) {
                private boolean hovered = false;
                { addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                    public void mouseClicked(MouseEvent e) {
                        selectedFlight = f;
                        seatSelectionView.refresh();
                        cardLayout.show(mainContainer, "Seats");
                    }
                });
                    setCursor(new Cursor(Cursor.HAND_CURSOR)); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(hovered ? new Color(235, 244, 255) : PURE_WHITE);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2d.setColor(new Color(215, 230, 248));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                    super.paintComponent(g);
                }
            };
            row.setOpaque(false);
            row.setPreferredSize(new Dimension(900, 64));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

            JLabel idLbl = new JLabel(f.id);
            idLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
            idLbl.setForeground(ELECTRIC_BLUE);
            idLbl.setBounds(10, 12, 80, 20);
            row.add(idLbl);

            JLabel airlineLbl = new JLabel(f.airline);
            airlineLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            airlineLbl.setForeground(NAVY_BLUE);
            airlineLbl.setBounds(90, 12, 115, 20);
            row.add(airlineLbl);

            JLabel routeLbl = new JLabel("<html><b>" + f.origin.substring(0,3).toUpperCase()
                    + "</b> <font color='#007AFF'>✈</font> <b>"
                    + f.dest.substring(0, Math.min(3, f.dest.length())).toUpperCase() + "</b></html>");
            routeLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            routeLbl.setForeground(NAVY_BLUE);
            routeLbl.setBounds(210, 6, 160, 24);
            row.add(routeLbl);

            JLabel destFull = new JLabel(f.dest);
            destFull.setFont(new Font("SansSerif", Font.PLAIN, 11));
            destFull.setForeground(SLATE_GRAY);
            destFull.setBounds(210, 28, 200, 16);
            row.add(destFull);

            JLabel durLbl = new JLabel(f.duration);
            durLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            durLbl.setForeground(SLATE_GRAY);
            durLbl.setBounds(430, 12, 120, 20);
            row.add(durLbl);

            JLabel stopLbl = new JLabel(f.stops == 0 ? "Non-stop" : f.stops + " stop");
            stopLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            stopLbl.setForeground(f.stops == 0 ? SUCCESS_GREEN : new Color(234, 120, 40));
            stopLbl.setBounds(560, 12, 100, 20);
            row.add(stopLbl);

            JLabel priceLbl = new JLabel(String.format("₹ %,.0f", f.basePrice));
            priceLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
            priceLbl.setForeground(NAVY_BLUE);
            priceLbl.setBounds(668, 10, 140, 24);
            row.add(priceLbl);

            JLabel perPax = new JLabel("per person");
            perPax.setFont(new Font("SansSerif", Font.PLAIN, 10));
            perPax.setForeground(SLATE_GRAY);
            perPax.setBounds(668, 32, 100, 14);
            row.add(perPax);

            JLabel arrow = new JLabel("›");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
            arrow.setForeground(ELECTRIC_BLUE);
            arrow.setBounds(860, 18, 20, 24);
            row.add(arrow);

            return row;
        }
    }

    // ==========================================
    // SEAT SELECTION VIEW
    // ==========================================

    class SeatSelectionView extends JPanel {
        public SeatSelectionView() {
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        public void refresh() {
            removeAll();
            GlassCardPanel card = new GlassCardPanel();
            card.setLayout(new BorderLayout(10, 10));
            card.setPreferredSize(new Dimension(860, 660));

            JPanel hdr = new JPanel(new BorderLayout());
            hdr.setOpaque(false);
            hdr.add(createHeaderLabel("Select Your Seat"), BorderLayout.WEST);
            JLabel flightInfo = new JLabel(selectedFlight.id + "  |  "
                    + selectedFlight.airline + "  |  "
                    + selectedFlight.origin + " → " + selectedFlight.dest);
            flightInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
            flightInfo.setForeground(SLATE_GRAY);
            hdr.add(flightInfo, BorderLayout.SOUTH);
            card.add(hdr, BorderLayout.NORTH);

            JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
            legend.setOpaque(false);
            legend.add(makeLegendItem(FIRST_CLASS_COLOR, "First Class (+₹8,000)"));
            legend.add(makeLegendItem(BUSINESS_COLOR,    "Business (+₹4,000)"));
            legend.add(makeLegendItem(ECONOMY_COLOR,     "Economy (base fare)"));
            legend.add(makeLegendItem(new Color(220,220,220), "Booked"));
            card.add(legend, BorderLayout.BEFORE_FIRST_LINE);

            JPanel seatContainer = new JPanel();
            seatContainer.setLayout(new BoxLayout(seatContainer, BoxLayout.Y_AXIS));
            seatContainer.setOpaque(false);

            seatContainer.add(createSectionHeader("FIRST CLASS", FIRST_CLASS_COLOR));
            seatContainer.add(generateSeatRows(1, 2, 2, 2, "First Class", 8000.0, FIRST_CLASS_COLOR));

            seatContainer.add(createSectionHeader("BUSINESS CLASS", BUSINESS_COLOR));
            seatContainer.add(generateSeatRows(3, 3, 2, 2, "Business", 4000.0, BUSINESS_COLOR));

            seatContainer.add(createSectionHeader("ECONOMY CLASS", ECONOMY_COLOR));
            seatContainer.add(generateSeatRows(6, 6, 3, 3, "Economy", 0.0, ECONOMY_COLOR));

            JScrollPane scrollPane = new JScrollPane(seatContainer);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            card.add(scrollPane, BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            south.setOpaque(false);
            JButton backBtn = createOutlineButton("← Back to Flights");
            backBtn.addActionListener(e -> cardLayout.show(mainContainer, "Search"));
            south.add(backBtn);

            card.add(south, BorderLayout.SOUTH);
            add(card);
            revalidate();
            repaint();
        }

        private JPanel makeLegendItem(Color c, String label) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            p.setOpaque(false);
            JLabel box = new JLabel("  ");
            box.setBackground(c);
            box.setOpaque(true);
            box.setPreferredSize(new Dimension(18, 14));
            JLabel txt = new JLabel(label);
            txt.setFont(new Font("SansSerif", Font.PLAIN, 12));
            txt.setForeground(SLATE_GRAY);
            p.add(box); p.add(txt);
            return p;
        }

        private JPanel createSectionHeader(String title, Color color) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            p.setOpaque(false);
            JLabel l = new JLabel(title);
            l.setFont(new Font("SansSerif", Font.BOLD, 13));
            l.setForeground(color.darker());
            l.setBorder(new EmptyBorder(8, 0, 2, 0));
            p.add(l);
            return p;
        }

        private JPanel generateSeatRows(int startRow, int numRows, int seatsLeft, int seatsRight,
                                        String sClass, double modifier, Color classColor) {
            JPanel section = new JPanel();
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            section.setOpaque(false);
            String[] letters = {"A","B","C","D","E","F","G","H"};

            for (int r = 0; r < numRows; r++) {
                int rowNum = startRow + r;
                JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                rowPanel.setOpaque(false);

                JLabel rowLbl = new JLabel(String.valueOf(rowNum));
                rowLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
                rowLbl.setForeground(SLATE_GRAY);
                rowLbl.setPreferredSize(new Dimension(20, 60));
                rowLbl.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanel.add(rowLbl);

                for (int s = 0; s < seatsLeft; s++)
                    rowPanel.add(createSeatButton(rowNum + letters[s], sClass, modifier, classColor));
                rowPanel.add(Box.createHorizontalStrut(30));
                for (int s = 0; s < seatsRight; s++)
                    rowPanel.add(createSeatButton(rowNum + letters[seatsLeft + s], sClass, modifier, classColor));

                section.add(rowPanel);
            }
            return section;
        }

        private JButton createSeatButton(String seatId, String sClass, double modifier, Color baseColor) {
            double totalSeatPrice = selectedFlight.basePrice + modifier;
            Seat seat = new Seat(seatId, sClass, modifier, totalSeatPrice);
            if (Math.random() > 0.75) seat.isBooked = true;

            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(68, 62));
            btn.setMargin(new Insets(0,0,0,0));
            btn.setFocusPainted(false);

            if (seat.isBooked) {
                btn.setText("<html><center><font color='gray' size='1'><b>" + seatId + "</b><br>✗</font></center></html>");
                btn.setBackground(new Color(220,220,220));
                btn.setEnabled(false);
                btn.setToolTipText("Seat " + seatId + " is already booked");
            } else {
                btn.setText("<html><center><b>" + seatId + "</b><br><font size='1'>₹" + (int)totalSeatPrice + "</font></center></html>");
                btn.setBackground(baseColor);
                btn.setToolTipText("Click to select " + sClass + " seat " + seatId);
                btn.addActionListener(e -> {
                    selectedSeat = seat;
                    checkoutView.refresh();
                    cardLayout.show(mainContainer, "Checkout");
                });
            }
            return btn;
        }
    }

    // ==========================================
    // CHECKOUT VIEW
    // ==========================================

    class CheckoutView extends JPanel {
        public CheckoutView() {
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        public void refresh() {
            removeAll();
            GlassCardPanel card = new GlassCardPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setPreferredSize(new Dimension(560, 480));

            JLabel heading = createHeaderLabel("Booking Summary");
            heading.setAlignmentX(Component.CENTER_ALIGNMENT);
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(heading);
            card.add(Box.createVerticalStrut(20));

            JPanel summary = new JPanel(new GridLayout(0, 2, 20, 10));
            summary.setOpaque(false);
            summary.setMaximumSize(new Dimension(460, 300));
            summary.setAlignmentX(Component.CENTER_ALIGNMENT);

            addSummaryRow(summary, "Route",     selectedFlight.origin + " → " + selectedFlight.dest);
            addSummaryRow(summary, "Flight",    selectedFlight.id + " (" + selectedFlight.airline + ")");
            addSummaryRow(summary, "Seat",      selectedSeat.seatNum + " – " + selectedSeat.seatClass);
            addSummaryRow(summary, "Duration",  selectedFlight.duration);
            addSummaryRow(summary, "Stops",     selectedFlight.stops == 0 ? "Non-stop" : selectedFlight.stops + " stop(s)");
            addSummaryRow(summary, "Date",      LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            addSummaryRow(summary, "Passenger", currentUser.name);

            card.add(summary);
            card.add(Box.createVerticalStrut(20));

            JSeparator divider = new JSeparator();
            divider.setMaximumSize(new Dimension(460, 1));
            divider.setForeground(new Color(215, 225, 240));
            card.add(divider);
            card.add(Box.createVerticalStrut(16));

            JPanel totalRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            totalRow.setOpaque(false);
            totalRow.setMaximumSize(new Dimension(460, 50));
            totalRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel totalLabel = new JLabel("Total Due:");
            totalLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
            totalLabel.setForeground(NAVY_BLUE);
            JLabel totalAmt = new JLabel(String.format("₹ %,.2f", selectedSeat.totalPrice));
            totalAmt.setFont(new Font("SansSerif", Font.BOLD, 26));
            totalAmt.setForeground(ELECTRIC_BLUE);
            totalRow.add(totalLabel);
            totalRow.add(totalAmt);
            card.add(totalRow);
            card.add(Box.createVerticalStrut(8));

            int milesToEarn = (int)(selectedSeat.totalPrice / 10);
            JLabel milesEarn = new JLabel("✦  You'll earn " + milesToEarn + " frequent flyer miles");
            milesEarn.setFont(new Font("SansSerif", Font.ITALIC, 13));
            milesEarn.setForeground(SUCCESS_GREEN);
            milesEarn.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(milesEarn);
            card.add(Box.createVerticalStrut(24));

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            btnPanel.setOpaque(false);
            btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton payBtn = createStyledButton("✓  Pay Securely");
            payBtn.addActionListener(e -> processPayment());
            JButton backBtn = createOutlineButton("Cancel");
            backBtn.addActionListener(e -> cardLayout.show(mainContainer, "Seats"));

            btnPanel.add(payBtn);
            btnPanel.add(backBtn);
            card.add(btnPanel);

            add(card);
            revalidate();
            repaint();
        }

        private void addSummaryRow(JPanel panel, String key, String value) {
            JLabel k = new JLabel(key);
            k.setFont(new Font("SansSerif", Font.PLAIN, 13));
            k.setForeground(SLATE_GRAY);
            k.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel v = new JLabel(value);
            v.setFont(new Font("SansSerif", Font.BOLD, 13));
            v.setForeground(NAVY_BLUE);
            v.setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(k);
            panel.add(v);
        }

        private void processPayment() {
            JOptionPane.showMessageDialog(this, "Processing via secure gateway…", "Payment", JOptionPane.INFORMATION_MESSAGE);
            generatedPNR = "PNR" + (new Random().nextInt(900000) + 100000);
            currentUser.miles += (int)(selectedSeat.totalPrice / 10);
            ticketView.refresh();
            cardLayout.show(mainContainer, "Ticket");
        }
    }

    // ==========================================
    // TICKET VIEW
    // ==========================================

    class TicketView extends JPanel {
        private VirtualTicketPanel ticketPanel;

        public TicketView() {
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        public void refresh() {
            removeAll();

            JPanel wrapper = new JPanel(new BorderLayout(0, 16));
            wrapper.setOpaque(false);
            wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

            JLabel title = createHeaderLabel("Your Boarding Pass is Ready  ✓");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setForeground(SUCCESS_GREEN);
            wrapper.add(title, BorderLayout.NORTH);

            ticketPanel = new VirtualTicketPanel();
            ticketPanel.setPreferredSize(new Dimension(860, 360));
            wrapper.add(ticketPanel, BorderLayout.CENTER);

            JPanel btnPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
            btnPan.setOpaque(false);

            JButton downloadBtn = createStyledButton("⬇  Download Ticket");
            downloadBtn.setPreferredSize(new Dimension(200, 42));
            downloadBtn.addActionListener(e -> downloadTicket());

            JButton dashBtn = createOutlineButton("← Dashboard");
            dashBtn.addActionListener(e -> cardLayout.show(mainContainer, "Dashboard"));

            JButton newSearchBtn = createOutlineButton("Search Again");
            newSearchBtn.addActionListener(e -> {
                searchView.performSearch("");
                cardLayout.show(mainContainer, "Search");
            });

            btnPan.add(downloadBtn);
            btnPan.add(dashBtn);
            btnPan.add(newSearchBtn);
            wrapper.add(btnPan, BorderLayout.SOUTH);

            add(wrapper);
            revalidate();
            repaint();
        }

        private void downloadTicket() {
            if (ticketPanel == null) return;
            int w = 900, h = 380;
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(SKY_BLUE);
            g2d.fillRect(0, 0, w, h);
            ticketPanel.paintTicket(g2d, w, h);
            g2d.dispose();

            String fileName = "BoardingPass_" + generatedPNR + ".png";
            File outFile = new File(System.getProperty("user.home"), fileName);
            try {
                ImageIO.write(img, "PNG", outFile);
                JOptionPane.showMessageDialog(this,
                        "Boarding pass saved to:\n" + outFile.getAbsolutePath(),
                        "Download Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not save file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        class VirtualTicketPanel extends JPanel {
            public VirtualTicketPanel() { setOpaque(false); }

            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintTicket((Graphics2D) g, getWidth(), getHeight());
            }

            public void paintTicket(Graphics2D g2d, int panelW, int panelH) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int margin = 20;
                int w = panelW - margin * 2;
                int h = panelH - margin * 2;
                int x = margin, y = margin;
                int stubW = 200;
                int stubX = w - stubW;

                g2d.setColor(new Color(0,0,0,22));
                g2d.fillRoundRect(x+5, y+5, w, h, 22, 22);

                g2d.setColor(PURE_WHITE);
                g2d.fillRoundRect(x, y, w, h, 22, 22);

                g2d.setColor(NAVY_BLUE);
                g2d.fillRoundRect(x, y, w, 54, 22, 22);
                g2d.fillRect(x, y + 30, w, 24);

                int perfX = x + stubX;
                g2d.setColor(new Color(200, 215, 230));
                Stroke dashed = new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
                g2d.setStroke(dashed);
                g2d.drawLine(perfX, y + 54, perfX, y + h);
                g2d.setStroke(new BasicStroke(1));

                g2d.setColor(SKY_BLUE);
                g2d.fillOval(perfX - 14, y - 14, 28, 28);
                g2d.fillOval(perfX - 14, y + h - 14, 28, 28);

                g2d.setColor(PURE_WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
                g2d.drawString("✈  BOARDING PASS  —  " + selectedFlight.airline.toUpperCase(), x + 18, y + 33);

                Color classCol = selectedSeat.seatClass.equalsIgnoreCase("First Class") ? FIRST_CLASS_COLOR
                        : selectedSeat.seatClass.equalsIgnoreCase("Business") ? new Color(200,160,255)
                          : new Color(173,216,230);
                g2d.setColor(classCol);
                g2d.fillRoundRect(perfX + 12, y + 12, stubW - 24, 28, 8, 8);
                g2d.setColor(NAVY_BLUE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2d.drawString(selectedSeat.seatClass.toUpperCase(), perfX + 20, y + 31);

                g2d.setColor(NAVY_BLUE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 54));
                g2d.drawString(selectedFlight.origin.substring(0, 3).toUpperCase(), x + 16, y + 118);

                g2d.setColor(ELECTRIC_BLUE);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 32));
                g2d.drawString("———  ✈  ———", x + 145, y + 108);

                g2d.setColor(NAVY_BLUE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 54));
                int destLen = Math.min(3, selectedFlight.dest.length());
                g2d.drawString(selectedFlight.dest.substring(0, destLen).toUpperCase(), x + 440, y + 118);

                g2d.setColor(SLATE_GRAY);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2d.drawString(selectedFlight.depTime, x + 16, y + 134);
                g2d.drawString(selectedFlight.arrTime, x + 440, y + 134);

                int dy = y + 162;
                drawDetail(g2d, "PASSENGER", currentUser.name.toUpperCase(), x + 16, dy);
                drawDetail(g2d, "FLIGHT",    selectedFlight.id,              x + 16, dy + 52);
                drawDetail(g2d, "PNR",       generatedPNR,                   x + 180, dy + 52);
                drawDetail(g2d, "DATE",
                        LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        x + 320, dy + 52);
                drawDetail(g2d, "DURATION",  selectedFlight.duration,        x + 460, dy + 52);

                drawDetail(g2d, "SEAT",  selectedSeat.seatNum, perfX + 18, y + 80);
                drawDetail(g2d, "GATE",  "G4",                 perfX + 100, y + 80);
                drawDetail(g2d, "FARE",
                        String.format("₹%,.0f", selectedSeat.totalPrice),
                        perfX + 18, y + 140);
                drawDetail(g2d, "STOPS",
                        selectedFlight.stops == 0 ? "Non-stop" : selectedFlight.stops + " stop",
                        perfX + 18, y + 190);

                g2d.setColor(Color.BLACK);
                Random r = new Random(generatedPNR.hashCode());
                int barcodeY = y + h - 90;
                for (int i = 0; i < 155; i += (r.nextInt(5) + 2)) {
                    int thick = r.nextInt(3) + 1;
                    g2d.fillRect(perfX + 18 + i, barcodeY, thick, 58);
                }
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                g2d.setColor(SLATE_GRAY);
                g2d.drawString(generatedPNR, perfX + 46, barcodeY + 70);
            }

            private void drawDetail(Graphics2D g2d, String label, String value, int x, int y) {
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2d.setColor(SLATE_GRAY);
                g2d.drawString(label, x, y);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
                g2d.setColor(Color.BLACK);
                g2d.drawString(value, x, y + 20);
            }
        }
    }

}