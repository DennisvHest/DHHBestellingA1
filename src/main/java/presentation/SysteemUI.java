package presentation;

import domain.Dish;
import domain.KitchenOrder;
import domain.RestaurantOrder;
import manager.DishManager;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import manager.OrderManager;

/**
 *
 * @author Mathijs, Dennis
 */
public class SysteemUI extends JFrame {

    private JFrame frame;
    private JPanel centerMenu, receiptPanel;
    private JSplitPane menuPane;
    private List<Component> panelList;
    private JTabbedPane menuTabbedPane;
    private OrderSummaryPanel orderSummaryPanel;
    private OrderOverviewPanel orderOverviewPanel;
    private DishManager dishManager;
    private OrderManager orderManager;

    public SysteemUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setLayout(new BorderLayout());
        frame.setTitle("De Hartige Hap");

        panelList = new ArrayList<>();

        dishManager = new DishManager();
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
        receiptPanel = new JPanel();

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
            for (Dish dish : dishManager.getDishListBySort("Appetizer")) {
                add(createDishPanel(dish));
            }
        }
    }

    class MainCoursePanel extends JPanel {

        public MainCoursePanel() {
            //Display every main course
            for (Dish dish : dishManager.getDishListBySort("MainCourse")) {
                add(createDishPanel(dish));
            }
        }
    }

    class DessertPanel extends JPanel {

        public DessertPanel() {
            //Display every dessert
            for (Dish dish : dishManager.getDishListBySort("Dessert")) {
                add(createDishPanel(dish));
            }
        }
    }

    class DrinkPanel extends JPanel {

        public DrinkPanel() {
            //Display every drink
            for (Dish dish : dishManager.getDishListBySort("Drink")) {
                add(createDishPanel(dish));
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

            ordersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            add(ordersPanel);
        }

        public void refreshOverviewPanel() {
            ordersPanel.removeAll();

            if (orderManager.pendingOrderExist()) {
                for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                    ordersPanel.add(createPendingOrderPanel(kitchenOrder));
                }
            }
        }

        public JPanel createPendingOrderPanel(KitchenOrder kitchenOrder) {
            JPanel pendingOrderPanel = new JPanel();
            pendingOrderPanel.setLayout(new BoxLayout(pendingOrderPanel, BoxLayout.X_AXIS));
            pendingOrderPanel.setPreferredSize(new Dimension(1000, 100));
            pendingOrderPanel.setBackground(Color.white);

            Dish dishInOrder = kitchenOrder.getDish();

            pendingOrderPanel.add(Box.createGlue());
            pendingOrderPanel.add(new JLabel(dishInOrder.getNameDish()));
            pendingOrderPanel.add(Box.createGlue());
            pendingOrderPanel.add(new JLabel(dishInOrder.getSortDish()));
            pendingOrderPanel.add(Box.createGlue());
            pendingOrderPanel.add(new JLabel("Aantal"));

            JSpinner amountSpinner = new JSpinner();
            amountSpinner.setMaximumSize(new Dimension(50, 50));
            pendingOrderPanel.add(amountSpinner);

            pendingOrderPanel.add(Box.createGlue());

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
            pendingOrderPanel.add(deleteOrderButton);

            pendingOrderPanel.setAlignmentX(CENTER_ALIGNMENT);

            return pendingOrderPanel;
        }
    }

    public void changePanel(String panel) {
        CardLayout cl = (CardLayout) (centerMenu.getLayout());
        cl.show(centerMenu, panel);
    }

    public JPanel createDishPanel(Dish dish) {
        JPanel dishPanel = new JPanel();
        List<JLabel> labels = new ArrayList<>();

        dishPanel.setPreferredSize(new Dimension(300, 400));

        //Everything will be displayed vertically
        dishPanel.setLayout(new BorderLayout());

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.setBackground(Color.white);

        labels.add(new JLabel(dish.getNameDish()));
        labels.add(new JLabel(dish.getDescriptionDish()));
        labels.add(new JLabel("€ " + String.format("%.2f", dish.getpriceDish())));

        for (JLabel label : labels) {
            label.setAlignmentX(CENTER_ALIGNMENT);
            descriptionPanel.add(label);
        }

        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(Color.white);

        JButton addButton = new JButton("Voeg toe");
        addButton.addActionListener((ActionEvent e) -> {
            //If a pending order does not exists, create one.
            if (!orderManager.pendingOrderExist()) {
                RestaurantOrder pendingOrder = new RestaurantOrder(1);
                orderManager.addOrder(pendingOrder);
            }

            boolean createOrder = true;

            //Add a new KitchenOrder to the pending order
            for (KitchenOrder kitchenOrder : orderManager.getPendingOrder().getKitchenOrders()) {
                if (kitchenOrder.getDish() == dish) {
                    createOrder = false;
                }
            }

            if (createOrder == true) {
                orderManager.getPendingOrder().addKitchenOrder(new KitchenOrder(1, dish, 1));
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
}
