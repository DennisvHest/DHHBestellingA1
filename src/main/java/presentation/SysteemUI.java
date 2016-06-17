package presentation;

import domain.BarOrder;
import domain.Dish;
import domain.Drink;
import domain.Ingredient;
import domain.Item;
import domain.ItemOrder;
import domain.KitchenOrder;
import domain.RestaurantOrder;
import domain.Table;
import manager.ItemManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import manager.OrderManager;
import manager.TableManager;

/**
 *
 * @author Mathijs, Dennis
 */
public class SysteemUI extends JFrame {

    private JFrame frame;
    private JPanel centerMenu;
    private JSplitPane menuPane;
    private List<Component> panelList;
    private JTabbedPane menuTabbedPane;
    private OrderSummaryPanel orderSummaryPanel;
    private OrderOverviewPanel orderOverviewPanel;
    private ReceiptPanel receiptPanel;
    private ItemManager itemManager;
    private OrderManager orderManager;
    private TableManager tableManager;
    private JDialog helpDialog, welcomeDialog;

    public SysteemUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setLayout(new BorderLayout());
        frame.setTitle("De Hartige Hap");

        panelList = new ArrayList<>();

        itemManager = new ItemManager();
        itemManager.findMenuItems();

        orderManager = new OrderManager();

        tableManager = new TableManager();
        tableManager.setTable(new Table(1));

        //Navigation bar with buttons
        frame.add(new NavBarPanel(), BorderLayout.NORTH);

        //Categories with dishes / drinks
        menuTabbedPane = new JTabbedPane();
        menuTabbedPane.add("Voorgerechten", new JScrollPane(new AppetizerPanel()));
        menuTabbedPane.add("Hoofdgerechten", new JScrollPane(new MainCoursePanel()));
        menuTabbedPane.add("Nagerechten", new JScrollPane(new DessertPanel()));
        menuTabbedPane.add("Dranken", new JScrollPane(new DrinkPanel()));
        menuTabbedPane.setMinimumSize(new Dimension(1100, 1000));

        orderSummaryPanel = new OrderSummaryPanel();
        menuPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuTabbedPane, orderSummaryPanel);
        menuPane.setDividerLocation(2000);

        //Menu with list of ordered items
        orderOverviewPanel = new OrderOverviewPanel();

        //Menu with receipt
        receiptPanel = new ReceiptPanel();

        //Panel with CardLayout that holds all other menus
        centerMenu = new JPanel();
        centerMenu.setLayout(new CardLayout());
        centerMenu.add("menuPane", menuPane);
        centerMenu.add("orderOverviewPanel", orderOverviewPanel);
        centerMenu.add("receiptPanel", receiptPanel);
        frame.add(centerMenu, BorderLayout.CENTER);
        
        welcomeDialog = createWelcomeDialog();
        welcomeDialog.setVisible(true);

        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    class NavBarPanel extends JPanel {

        private JButton helpButton;

        public NavBarPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.white);

            add(new NavButtonPanel(), BorderLayout.WEST);

            add(helpButton, BorderLayout.EAST);
        }

        class NavButtonPanel extends JPanel {

            private JButton menuButton, orderedItemButton, receiptButton;
            private List<JButton> navBarButtons;

            public NavButtonPanel() {
                setLayout(new GridLayout(1, 3));

                //Menu buttons
                menuButton = new JButton("Menu");
                menuButton.addActionListener((ActionEvent e) -> {
                    changePanel("menuPane");
                    menuTabbedPane.setVisible(true);
                });

                orderedItemButton = new JButton("Besteloverzicht");
                orderedItemButton.addActionListener((ActionEvent e) -> {
                    changePanel("orderOverviewPanel");
                });

                receiptButton = new JButton("Rekening");
                receiptButton.addActionListener((ActionEvent e) -> {
                    changePanel("receiptPanel");
                });

                helpDialog = createHelpDialog();
                helpDialog.setVisible(false);

                helpButton = new JButton("?");
                helpButton.addActionListener((ActionEvent e) -> {
                    helpDialog.setVisible(true);
                });

                //Array with all navbar buttons for styling and adding to the panel
                navBarButtons = new ArrayList<>();

                navBarButtons.add(menuButton);
                navBarButtons.add(orderedItemButton);
                navBarButtons.add(receiptButton);

                for (JButton button : navBarButtons) {
                    button.setMargin(new Insets(10, 10, 10, 10));
                    add(button);
                }
            }
        }
    }

    class AppetizerPanel extends JPanel {

        public AppetizerPanel() {
            //Display every appetizer
            for (Dish dish : itemManager.getDishListBySort("Appetizer")) {
                add(createItemPanel(dish));
            }
        }
    }

    class MainCoursePanel extends JPanel {

        public MainCoursePanel() {
            //Display every main course
            for (Dish dish : itemManager.getDishListBySort("MainCourse")) {
                add(createItemPanel(dish));
            }
        }
    }

    class DessertPanel extends JPanel {

        public DessertPanel() {
            //Display every dessert
            for (Dish dish : itemManager.getDishListBySort("Dessert")) {
                add(createItemPanel(dish));
            }
        }
    }

    class DrinkPanel extends JPanel {

        public DrinkPanel() {
            //Display every drink
            for (Drink drink : itemManager.getDrinkList()) {
                add(createItemPanel(drink));
            }
        }
    }

    class OrderSummaryPanel extends JPanel {

        private JTextArea orderSumArea;

        public OrderSummaryPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.white);
            add(new JLabel("Toegevoegde gerechten:"));
            setPreferredSize(new Dimension(400, menuTabbedPane.getHeight()));

            orderSumArea = new JTextArea();
            orderSumArea.setEditable(false);
            add(orderSumArea);
        }

        public void setSumText(String text) {
            orderSumArea.setText(text);
        }
    }

    class OrderOverviewPanel extends JPanel {

        private JPanel ordersPanel;

        //Menu that shows the pending order and the previously sent orders
        public OrderOverviewPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(new JLabel("Bestelling in afwachting van bevestiging"));

            ordersPanel = new JPanel();
            ordersPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            add(ordersPanel);
        }

        public void refreshOverviewPanel() {
            ordersPanel.removeAll();

            if (orderManager.pendingOrderExist()) {
                for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                    ordersPanel.add(createOrderPanel(kitchenOrder, "pending"));
                }

                for (BarOrder barOrder : orderManager.getPendingOrder().getBarOrders()) {
                    ordersPanel.add(createOrderPanel(barOrder, "pending"));
                }

                if (!orderManager.getPendingOrder().getItemOrders().isEmpty()) {
                    JButton confirmButton = new JButton("Bevestig");
                    confirmButton.addActionListener((ActionEvent e) -> {
                        //Insert kitchen- and drinkOrders into database
                        orderManager.insertItemOrder();

                        //Change the order status to placed
                        orderManager.getPendingOrder().setOrderStatus("placed");

                        //Refresh the OrderOverviewPanel
                        refreshOverviewPanel();
                        changePanel("menuPane");
                        changePanel("orderOverviewPanel");

                        //Refresh the OrderSummary text
                        orderSummaryPanel.setSumText(orderManager.printPendingOrders());

                        //Refresh the OrderReceiptPanel
                        receiptPanel.refreshOrderReceiptPanel();
                    });

                    JPanel confirmPanel = new JPanel();
                    confirmPanel.setPreferredSize(new Dimension(1000, 100));
                    confirmPanel.add(confirmButton);
                    ordersPanel.add(confirmPanel);
                }
            }

            if (orderManager.placedOrderExist()) {
                for (RestaurantOrder order : orderManager.getPlacedOrders()) {
                    JPanel orderHeaderPanel = new JPanel();
                    orderHeaderPanel.setPreferredSize(new Dimension(1000, 20));
                    orderHeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    orderHeaderPanel.add(new JLabel("Geplaatste bestelling #" + Integer.toString(order.getOrderNr())));
                    ordersPanel.add(orderHeaderPanel);

                    for (KitchenOrder kitchenOrder : order.getKitchenOrders()) {
                        ordersPanel.add(createOrderPanel(kitchenOrder, "placed"));
                    }

                    for (BarOrder barOrder : order.getBarOrders()) {
                        ordersPanel.add(createOrderPanel(barOrder, "placed"));
                    }
                }
            }
        }

        public JPanel createOrderPanel(ItemOrder itemOrder, String orderType) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.X_AXIS));
            orderPanel.setPreferredSize(new Dimension(1000, 100));

            Item itemInOrder = itemOrder.getItem();

            orderPanel.add(Box.createGlue());
            orderPanel.add(new JLabel(itemInOrder.getName()));
            orderPanel.add(Box.createGlue());

            if (itemInOrder instanceof Dish) {
                orderPanel.add(new JLabel(((Dish) itemInOrder).getSortDish()));
            } else {
                orderPanel.add(Box.createGlue());
            }

            orderPanel.add(Box.createGlue());
            orderPanel.add(new JLabel("Aantal"));

            JSpinner amountSpinner = new JSpinner(new SpinnerNumberModel(itemOrder.getAmount(), 1, 100, 1));
            amountSpinner.setMaximumSize(new Dimension(50, 50));
            amountSpinner.addChangeListener((ChangeEvent e) -> {
                itemOrder.setAmount((int) amountSpinner.getValue());
                //Refresh the OrderSummary text
                orderSummaryPanel.setSumText(orderManager.printPendingOrders());
            });
            orderPanel.add(amountSpinner);

            orderPanel.add(Box.createGlue());

            JButton deleteOrderButton = new JButton("X");
            deleteOrderButton.setMinimumSize(new Dimension(100, 100));
            deleteOrderButton.addActionListener((ActionEvent e) -> {
                orderManager.getPendingOrder().removeItemOrder(itemOrder);

                //Refresh the order overview
                orderOverviewPanel.refreshOverviewPanel();
                changePanel("menuPane");
                changePanel("orderOverviewPanel");

                //Refresh the OrderSummary text
                orderSummaryPanel.setSumText(orderManager.printPendingOrders());
            });
            orderPanel.add(deleteOrderButton);

            orderPanel.setAlignmentX(CENTER_ALIGNMENT);

            if ("pending".equals(orderType)) {
                orderPanel.setBackground(Color.white);
            } else {
                orderPanel.setBackground(Color.gray);
                amountSpinner.setEnabled(false);
                deleteOrderButton.setEnabled(false);
            }

            return orderPanel;
        }
    }

    class ReceiptPanel extends JPanel {

        private JPanel orderReceiptsPanel, orderTotalPanel;

        public ReceiptPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(new JLabel("Rekening"));

            orderReceiptsPanel = new JPanel();
            orderReceiptsPanel.setMaximumSize(new Dimension(600, 9999));
            add(orderReceiptsPanel);

            orderTotalPanel = new JPanel();
            orderTotalPanel.setMaximumSize(new Dimension(200, 200));
            add(orderTotalPanel);
        }

        public void refreshOrderReceiptPanel() {
            orderReceiptsPanel.removeAll();
            orderTotalPanel.removeAll();

            //For every unpaid order, create a panel with order information
            for (RestaurantOrder order : orderManager.getOrders()) {
                if (!"pending".equals(order.getOrderStatus()) && !"payed".equals(order.getOrderStatus())) {
                    orderReceiptsPanel.add(createOrderReceiptPanel(order));
                }
            }

            double total = orderManager.getUnpaidTotal();
            orderTotalPanel.add(new JLabel("Totaal: € " + String.format("%.2f", total)));

            JButton payButton = new JButton("Betalen");
            payButton.addActionListener((ActionEvent e) -> {
                orderManager.payUnpaidOrders();
            });
            orderTotalPanel.add(payButton);
        }

        public JPanel createOrderReceiptPanel(RestaurantOrder order) {
            JPanel orderReceiptPanel = new JPanel();
            orderReceiptPanel.setLayout(new BoxLayout(orderReceiptPanel, BoxLayout.Y_AXIS));

            orderReceiptPanel.add(new JLabel("Niet Betaalde bestelling #" + Integer.toString(order.getOrderNr())));

            JPanel orderTable = new JPanel();
            orderTable.setLayout(new GridLayout(order.getItemOrders().size() + 1, 4, 10, 10));
            orderTable.add(new JLabel("Naam gerecht"));
            orderTable.add(new JLabel("Prijs per gerecht"));
            orderTable.add(new JLabel("Aantal"));
            orderTable.add(new JLabel("Totaalprijs gerecht"));

            for (ItemOrder itemOrder : order.getItemOrders()) {
                String name = itemOrder.getItem().getName();
                int amount = itemOrder.getAmount();
                double pricePerDish = itemOrder.getItem().getPrice();
                double totalPriceDish = pricePerDish * amount;

                orderTable.add(new JLabel(name));
                orderTable.add(new JLabel("€ " + String.format("%.2f", pricePerDish)));
                orderTable.add(new JLabel(Integer.toString(amount)));
                orderTable.add(new JLabel("€ " + String.format("%.2f", totalPriceDish)));
            }

            orderReceiptPanel.add(orderTable);

            return orderReceiptPanel;
        }
    }

    public void changePanel(String panel) {
        CardLayout cl = (CardLayout) (centerMenu.getLayout());
        cl.show(centerMenu, panel);
    }

    public JPanel createItemPanel(Item item) {
        JPanel itemPanel = new JPanel();
        List<JLabel> labels = new ArrayList<>();

        itemPanel.setPreferredSize(new Dimension(300, 400));

        //Everything will be displayed vertically
        itemPanel.setLayout(new BorderLayout());

        BufferedImage image = item.getImage();

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 30, 20, 240, 160, this);
            }
        };

        imagePanel.setPreferredSize(new Dimension(300, 200));
        imagePanel.setBackground(Color.white);

        itemPanel.add(imagePanel, BorderLayout.NORTH);

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(Color.white);

        labels.add(new JLabel(item.getName()));

        if (item instanceof Dish) {
            labels.add(new JLabel(((Dish) item).getDescriptionDish()));
        }

        labels.add(new JLabel("€ " + String.format("%.2f", item.getPrice())));

        for (JLabel label : labels) {
            label.setAlignmentX(CENTER_ALIGNMENT);
            descriptionPanel.add(label);
        }

        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(Color.white);

        if (item instanceof Dish) {
            JDialog moreInfoDialog = createMoreInfoDialog((Dish) item);
            moreInfoDialog.setVisible(false);

            JButton moreInfoButton = new JButton("Meer info");
            moreInfoButton.addActionListener((ActionEvent e) -> {
                moreInfoDialog.setVisible(true);
            });

            orderPanel.add(moreInfoButton);
        }

        JButton addButton = new JButton("Voeg toe");
        addButton.addActionListener((ActionEvent e) -> {
            //If a pending order does not exists, create one.
            if (!orderManager.pendingOrderExist()) {
                RestaurantOrder pendingOrder = new RestaurantOrder(tableManager.getTable().getTableNr());
                pendingOrder.setOrderNr(orderManager.getAutoIncrementValue("restaurantorder"));
                orderManager.addOrder(pendingOrder);
                orderManager.insertRestaurantOrder();
            }

            if (item instanceof Dish) {
                //If a KitchenOrder with this dish already exists it should not be created again
                boolean createOrder = true;

                for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                    if (kitchenOrder.getItem() == item) {
                        createOrder = false;
                    }
                }

                if (createOrder == true) {
                    //Add a new KitchenOrder to the pending order
                    orderManager.getPendingOrder().addKitchenOrder(new KitchenOrder(orderManager.getAutoIncrementValue("kitchenorder"), (Dish) item, 1));
                }
            }

            if (item instanceof Drink) {
                //If a BarOrder with this drink already exists it should not be created again
                boolean createOrder = true;

                for (BarOrder barOrder : orderManager.getPendingOrder().getBarOrders()) {
                    if (barOrder.getItem() == item) {
                        createOrder = false;
                    }
                }

                if (createOrder == true) {
                    //Add a new BarOrder to the pending order
                    orderManager.getPendingOrder().addBarOrder(new BarOrder(orderManager.getAutoIncrementValue("barorder"), (Drink) item, 1));
                }
            }

            //Refresh the OrderSummary text
            orderSummaryPanel.setSumText(orderManager.printPendingOrders());

            //Refresh the order overview
            orderOverviewPanel.refreshOverviewPanel();
        });

        orderPanel.add(addButton);

        itemPanel.add(descriptionPanel, BorderLayout.CENTER);
        itemPanel.add(orderPanel, BorderLayout.SOUTH);

        return itemPanel;
    }

    public JDialog createMoreInfoDialog(Dish dish) {
        JDialog moreInfoDialog = new JDialog();
        moreInfoDialog.setSize(350, 500);
        moreInfoDialog.getContentPane().setBackground(Color.white);
        moreInfoDialog.setUndecorated(true);
        moreInfoDialog.setLocationRelativeTo(null);
        moreInfoDialog.setAlwaysOnTop(true);
        moreInfoDialog.setLayout(new BoxLayout(moreInfoDialog.getContentPane(), BoxLayout.Y_AXIS));
        moreInfoDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        
        BufferedImage image = dish.getImage();
        
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 25, 20, 300, 200, this);
            }
        };
        
        imagePanel.setBackground(Color.white);
        imagePanel.setMaximumSize(new Dimension(350, 240));

        moreInfoDialog.add(imagePanel);
        
        moreInfoDialog.add(new JLabel(dish.getName()));
        
        JLabel descriptionLabel = new JLabel("Beschrijving:");
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        moreInfoDialog.add(descriptionLabel);
        
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setMaximumSize(new Dimension(370, 100));
        descriptionArea.setText(dish.getDescriptionDish());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMargin(new Insets(0, 10, 0, 10));
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionScroll.setBorder(null);
        descriptionScroll.setMaximumSize(new Dimension(390, 100));
        
        moreInfoDialog.add(descriptionScroll);
        
        JPanel ingredientInfoPanel = new JPanel();
        ingredientInfoPanel.setLayout(new BoxLayout(ingredientInfoPanel, BoxLayout.X_AXIS));
        
        String ingredients = "Ingredienten:\n";
        
        for (Ingredient ingredient : dish.getIngredients()) {
            ingredients += "-" + ingredient.getName() + "\n";
        }
        
        String allergies = "Kan schadelijk zijn voor mensen met:\n";
        
        for (Ingredient ingredient : dish.getIngredients()) {
            if (!"".equals(ingredient.getAllergy())) {
                allergies += "-" + ingredient.getAllergy() + "\n";
            }
        }
        
        JTextArea ingredientsArea = new JTextArea();
        ingredientsArea.setText(ingredients);
        ingredientsArea.setMaximumSize(new Dimension(175, 150));
        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        ingredientsArea.setMargin(new Insets(0, 10, 0, 10));
        
        JTextArea allergiesArea = new JTextArea();
        allergiesArea.setText(allergies);
        allergiesArea.setMaximumSize(new Dimension(175, 150));
        allergiesArea.setEditable(false);
        allergiesArea.setLineWrap(true);
        allergiesArea.setWrapStyleWord(true);
        
        ingredientInfoPanel.add(ingredientsArea);
        ingredientInfoPanel.add(allergiesArea);
        
        moreInfoDialog.add(ingredientInfoPanel);

        JButton closeButton = new JButton("Terug");
        closeButton.addActionListener((ActionEvent e) -> {
            moreInfoDialog.setVisible(false);
        });

        moreInfoDialog.add(closeButton);

        return moreInfoDialog;
    }

    public JDialog createHelpDialog() {
        JDialog newHelpDialog = new JDialog();
        newHelpDialog.setSize(1000, 700);
        newHelpDialog.getContentPane().setBackground(Color.white);
        newHelpDialog.setUndecorated(true);
        newHelpDialog.setLocationRelativeTo(null);
        newHelpDialog.setAlwaysOnTop(true);
        newHelpDialog.setLayout(new BoxLayout(newHelpDialog.getContentPane(), BoxLayout.Y_AXIS));
        newHelpDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.blue, 3));

        newHelpDialog.add(new JLabel("     Informatie"));

        JPanel helpContentPanel = new JPanel();
        helpContentPanel.setLayout(new GridLayout(2, 2));

        InputStream menuImagePath = getClass().getResourceAsStream("/images/dhhMenuHelp.png");

        JPanel menuImagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                    g.drawImage(ImageIO.read(menuImagePath), 30, 20, 420, 280, this);
                } catch (IOException ex) {
                    System.err.println("Loading image failed: " + ex);
                }
            }
        };

        helpContentPanel.add(menuImagePanel);
        helpContentPanel.add(new JLabel(""
                + "<html>"
                + " <body>"
                + "     <p>Bestellen met een tablet! Hoe werkt dat?</p>"
                + " </body>"
                + "</html>"));

        JLabel dhhLabel = new JLabel("De Hartige Hap");
        dhhLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpContentPanel.add(dhhLabel);

        helpContentPanel.add(new JLabel(""));

        newHelpDialog.add(helpContentPanel);

        JButton helpButton = new JButton("Vraag hulp");
        helpButton.addActionListener((ActionEvent e) -> {
            tableManager.needHelp();
        });

        newHelpDialog.add(helpButton);

        JButton closeButton = new JButton("Terug");
        closeButton.addActionListener((ActionEvent e) -> {
            newHelpDialog.setVisible(false);
        });

        newHelpDialog.add(closeButton);

        return newHelpDialog;
    }
    
    public JDialog createWelcomeDialog() {
        JDialog welcomeDialog = new JDialog();
        welcomeDialog.setSize(1000, 700);
        welcomeDialog.getContentPane().setBackground(Color.white);
        welcomeDialog.setUndecorated(true);
        welcomeDialog.setLocationRelativeTo(null);
        welcomeDialog.setAlwaysOnTop(true);
        welcomeDialog.setLayout(new BoxLayout(welcomeDialog.getContentPane(), BoxLayout.Y_AXIS));
        welcomeDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        
        JLabel dhhLabel = new JLabel("Restaurant De Hartige Hap");
        dhhLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dhhLabel.setBorder(new LineBorder(Color.black, 5));
        dhhLabel.setFont(new Font("Serif", Font.BOLD, 80));
        
        welcomeDialog.add(dhhLabel);
        
        InputStream welkomImagePath = getClass().getResourceAsStream("/images/dhhWelkom.jpg");

        JPanel welkomImagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                    g.drawImage(ImageIO.read(welkomImagePath), 250, 10, this);
                } catch (IOException ex) {
                    System.err.println("Loading image failed: " + ex);
                }
            }
        };
        
        welkomImagePanel.setBackground(Color.white);
        
        JButton toMenuButton = new JButton("Menu");
        toMenuButton.addActionListener((ActionEvent e) -> {
            welcomeDialog.setVisible(false);
        });
        
        JButton toHelpButton = new JButton("Informatie");
        toHelpButton.addActionListener((ActionEvent e) -> {
            welcomeDialog.setVisible(false);
            helpDialog.setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(toMenuButton);
        buttonPanel.add(toHelpButton);
        
        welcomeDialog.add(welkomImagePanel);
        welcomeDialog.add(buttonPanel);
        
        return welcomeDialog;
    }
}
