package presentation;

import domain.Dish;
import domain.Drink;
import domain.Item;
import domain.KitchenOrder;
import domain.RestaurantOrder;
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
import java.util.List;
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
import javax.swing.event.ChangeEvent;
import manager.OrderManager;

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

                helpButton = new JButton("?");

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
                add(createDishPanel(dish));
            }
        }
    }

    class MainCoursePanel extends JPanel {

        public MainCoursePanel() {
            //Display every main course
            for (Dish dish : itemManager.getDishListBySort("MainCourse")) {
                add(createDishPanel(dish));
            }
        }
    }

    class DessertPanel extends JPanel {

        public DessertPanel() {
            //Display every dessert
            for (Dish dish : itemManager.getDishListBySort("Dessert")) {
                add(createDishPanel(dish));
            }
        }
    }

    class DrinkPanel extends JPanel {

        public DrinkPanel() {
            //Display every drink
            for (Drink drink : itemManager.getDrinkList()) {
                add(createDishPanel(drink));
            }
        }
    }

    class OrderSummaryPanel extends JPanel {

        private JTextArea orderSumArea;

        public OrderSummaryPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.white);
            add(new JLabel("Toegevoegde gerechten:"));

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

                if (!orderManager.getPendingOrder().getKitchenOrders().isEmpty()) {
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
                }
            }
        }

        public JPanel createOrderPanel(KitchenOrder kitchenOrder, String orderType) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.X_AXIS));
            orderPanel.setPreferredSize(new Dimension(1000, 100));

            Dish dishInOrder = kitchenOrder.getDish();

            orderPanel.add(Box.createGlue());
            orderPanel.add(new JLabel(dishInOrder.getName()));
            orderPanel.add(Box.createGlue());
            orderPanel.add(new JLabel(dishInOrder.getSortDish()));
            orderPanel.add(Box.createGlue());
            orderPanel.add(new JLabel("Aantal"));

            JSpinner amountSpinner = new JSpinner(new SpinnerNumberModel(kitchenOrder.getAmount(), 1, 100, 1));
            amountSpinner.setMaximumSize(new Dimension(50, 50));
            amountSpinner.addChangeListener((ChangeEvent e) -> {
                kitchenOrder.setAmount((int) amountSpinner.getValue());
            });
            orderPanel.add(amountSpinner);

            orderPanel.add(Box.createGlue());

            JButton deleteOrderButton = new JButton("X");
            deleteOrderButton.setMinimumSize(new Dimension(100, 100));
            deleteOrderButton.addActionListener((ActionEvent e) -> {
                orderManager.getPendingOrder().removeKitchenOrder(kitchenOrder);

                //Refresh the order overview
                orderOverviewPanel.refreshOverviewPanel();
                changePanel("menuPane");
                changePanel("orderOverviewPanel");

                //Refresh the OrderSummary text
                orderSummaryPanel.setSumText(orderManager.printPendingOrders());
            });
            orderPanel.add(deleteOrderButton);

            orderPanel.setAlignmentX(CENTER_ALIGNMENT);

            if (orderType == "pending") {
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
            orderTable.setLayout(new GridLayout(order.getKitchenOrders().size() + 1, 4, 10, 10));
            orderTable.add(new JLabel("Naam gerecht"));
            orderTable.add(new JLabel("Prijs per gerecht"));
            orderTable.add(new JLabel("Aantal"));
            orderTable.add(new JLabel("Totaalprijs gerecht"));

            for (KitchenOrder kitchenOrder : order.getKitchenOrders()) {
                String name = kitchenOrder.getDish().getName();
                int amount = kitchenOrder.getAmount();
                double pricePerDish = kitchenOrder.getDish().getPrice();
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

    public JPanel createDishPanel(Item item) {
        JPanel dishPanel = new JPanel();
        List<JLabel> labels = new ArrayList<>();

        dishPanel.setPreferredSize(new Dimension(300, 400));

        //Everything will be displayed vertically
        dishPanel.setLayout(new BorderLayout());

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

        JDialog moreInfoDialog = createMoreInfoDialog();
        moreInfoDialog.setVisible(false);

        if (item instanceof Dish) {
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
                RestaurantOrder pendingOrder = new RestaurantOrder(1);
                pendingOrder.setOrderNr(orderManager.getAutoIncrementValue());
                orderManager.addOrder(pendingOrder);
                orderManager.insertRestaurantOrder();
            }

            if (item instanceof Dish) {
                //If a KitchenOrder with this dish already exists it should not be created again
                boolean createOrder = true;

                for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                    if (kitchenOrder.getDish() == item) {
                        createOrder = false;
                    }
                }

                if (createOrder == true) {
                    //Add a new KitchenOrder to the pending order
                    orderManager.getPendingOrder().addKitchenOrder(new KitchenOrder(1, (Dish) item, 1));
                }
            }

            if (item instanceof Drink) {
                //If a BarOrder with this drink already exists it should not be created again
                boolean createOrder = true;

                for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                    if (kitchenOrder.getDish() == item) {
                        createOrder = false;
                    }
                }

                if (createOrder == true) {
                    //Add a new BarOrder to the pending order
                    orderManager.getPendingOrder().addKitchenOrder(new KitchenOrder(1, (Dish) item, 1));
                }
            }

            //Refresh the OrderSummary text
            orderSummaryPanel.setSumText(orderManager.printPendingOrders());

            //Refresh the order overview
            orderOverviewPanel.refreshOverviewPanel();
        });

        orderPanel.add(addButton);

        dishPanel.add(descriptionPanel, BorderLayout.CENTER);
        dishPanel.add(orderPanel, BorderLayout.SOUTH);

        return dishPanel;
    }

    public JDialog createMoreInfoDialog() {
        JDialog dialog = new JDialog();
        dialog.setSize(300, 400);
        dialog.getContentPane().setBackground(Color.white);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setLayout(new FlowLayout());
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.blue, 3));

        dialog.add(new JLabel("Hallo"));

        JButton closeButton = new JButton("Terug");
        closeButton.addActionListener((ActionEvent e) -> {
            dialog.setVisible(false);
        });

        dialog.add(closeButton);

        return dialog;
    }
}
