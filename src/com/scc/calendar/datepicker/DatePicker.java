package com.scc.calendar.datepicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

public class DatePicker extends Observable implements Runnable, WindowFocusListener {

    public static class DayLabel extends JLabel implements MouseInputListener, MouseMotionListener {
        private static final long serialVersionUID = -494342107456443141L;

        private final DatePicker parent;

        private Border oldBorder;

        public DayLabel(final DatePicker parent, final int day) {
            super(Integer.toString(day));
            this.parent = parent;
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(PLAIN_FONT);
            setOpaque(true);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(final MouseEvent e) {
            // JOptionPane.showMessageDialog(this,getText());
            parent.dayPicked(Integer.parseInt(getText()));
        }

        @Override
        public void mouseDragged(final MouseEvent e) {

        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            oldBorder = this.getBorder();
            final Border b = BorderFactory.createLineBorder(Color.orange);
            this.setBorder(b);
        }

        @Override
        public void mouseExited(final MouseEvent e) {
            this.setBorder(oldBorder);
        }

        @Override
        public void mouseMoved(final MouseEvent e) {
        }

        @Override
        public void mousePressed(final MouseEvent e) {

        }

        @Override
        public void mouseReleased(final MouseEvent e) {

        }

        public void setCurrentDayStyle() {
            setFont(PLAIN_FONT);
            setForeground(Color.blue);
            setBackground(Color.gray);
            setToolTipText(parent.getString("today", "Today"));
        }

        public void setSelectedDayStyle() {
            setFont(BOLD_FONT);
            setForeground(Color.green.darker());
            setBackground(Color.green.brighter());
            setBorder(BorderFactory.createLineBorder(Color.green.darker()));
            setToolTipText(parent.getString("selected.day", "Selected day"));
        }

        public void setWeekendStyle() {
            setFont(PLAIN_FONT);
            setForeground(Color.red);
        }

    }

    public static class MonthPanel extends JPanel {
        private static final long serialVersionUID = -3877620759226036289L;

        private final DatePicker parent_;

        public MonthPanel(final DatePicker parent, final Calendar c) {
            this.parent_ = parent;
            final GridLayout g = new GridLayout();
            g.setColumns(7);
            g.setRows(0);
            this.setLayout(g);

            final int firstDayOfWeek = c.getFirstDayOfWeek();
            final boolean us = firstDayOfWeek == Calendar.SUNDAY;

            /* get the days from the calendar */
            final Map<String, Integer> dayIndexes = c.getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.LONG,
                    parent.getLocale());
            final Map<Integer, String> idxDays = new HashMap<Integer, String>();
            for (final String dayNm : dayIndexes.keySet()) {
                idxDays.put(dayIndexes.get(dayNm), dayNm);
            }

            for (int i = 0; i < 7; i++) {
                int dayIdx = (firstDayOfWeek + i) % 7;
                if (dayIdx == 0) {
                    dayIdx = 7;
                }
                String day = idxDays.get(Integer.valueOf(dayIdx));
                day = day.substring(0, 2);

                final JLabel wd = new JLabel(day);
                wd.setOpaque(true);
                wd.setBackground(Color.lightGray);
                wd.setHorizontalAlignment(SwingConstants.CENTER);

                if (us) {
                    if (i == 0 || i == 6) {
                        wd.setForeground(Color.red);
                    }

                } else {
                    if (i == 5 || i == 6) {
                        wd.setForeground(Color.red);
                    }
                }

                this.add(wd);
            }
            setDaysOfMonth(c, us);
            this.setPreferredSize(new Dimension(197, 120));

        }

        private void setDaysOfMonth(final Calendar c, final boolean us) {
            final Calendar curr = Calendar.getInstance(parent_.getLocale());
            final int currdate = curr.get(Calendar.DAY_OF_MONTH);
            final int currmon = curr.get(Calendar.MONTH);
            final int curryear = curr.get(Calendar.YEAR);

            int seldate = -1;
            int selmon = -1;
            int selyear = -1;
            if (parent_.selectedDate_ != null) {
                seldate = parent_.selectedDate_.get(Calendar.DAY_OF_MONTH);
                selmon = parent_.selectedDate_.get(Calendar.MONTH);
                selyear = parent_.selectedDate_.get(Calendar.YEAR);
            }

            final int date = c.get(Calendar.DAY_OF_MONTH);
            final int mon = c.get(Calendar.MONTH);
            final int year = c.get(Calendar.YEAR);
            final int day = c.get(Calendar.DAY_OF_WEEK);
            final int start = (7 - (date - day) % 7 - (us ? 0 : 1)) % 7;
            final int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < start; i++) {
                final JLabel lbl = new JLabel("");
                add(lbl);
            }
            int pos = start;
            for (int i = 1; i <= days; i++) {
                final DayLabel lbl = new DayLabel(parent_, i);
                if (seldate == i && selmon == mon && selyear == year) {
                    lbl.setSelectedDayStyle();
                }
                if (currdate == i && currmon == mon && curryear == year) {
                    lbl.setCurrentDayStyle();
                }
                final int dayIndex = pos % 7;
                if (us) {
                    if (dayIndex == 0 || dayIndex == 6) {
                        lbl.setWeekendStyle();
                    }

                } else {
                    if (dayIndex == 5 || dayIndex == 6) {
                        lbl.setWeekendStyle();
                    }

                }
                add(lbl);
                pos++;
            }
        }
    }

    public static class NavigatePanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 1123148385364705929L;

        public static byte[] getImage(String fileName) {
            fileName = "com/scc/calendar/datepicker/" + fileName;
            InputStream is = null;

            try {
                is = new BufferedInputStream(DatePicker.class.getClassLoader().getResourceAsStream(fileName));
                final byte[] b = new byte[is.available()];
                is.read(b);
                return b;
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    is.close();
                } catch (final IOException e) {
                }
            }
        }

        private final DatePicker parent;

        private final JButton premon;

        private final JButton preyear;

        private final JButton nextmon;

        private final JButton nextyear;

        private JLabel lbl;

        private JComboBox monthBox;

        private JComboBox yearBox;

        private String[] months;

        private Integer[] years;

        private Box box;

        final int height = 10;

        public NavigatePanel(final DatePicker parent) {
            this.parent = parent;
            setBackground(Color.black);
            setLayout(new BorderLayout());

            final Dimension d = new Dimension(20, 20);

            Box box = new Box(BoxLayout.X_AXIS);
            preyear = new JButton();
            ( preyear).setRolloverEnabled(true);
            preyear.setToolTipText(parent.getString("pre.year", "Previous year."));
            ImageIcon icon = new ImageIcon(getImage("preyear.gif"), "<<");
            preyear.setIcon(icon);
            preyear.addActionListener(this);
            preyear.setPreferredSize(d);
            box.add(preyear);

            box.add(Box.createHorizontalStrut(1));

            premon = new JButton();
            premon.setToolTipText(parent.getString("pre.mon", "Previous Month"));
            icon = new ImageIcon(getImage("premon.gif"), "<");
            premon.setIcon(icon);
            premon.addActionListener(this);
            premon.setPreferredSize(d);
            box.add(premon);

            box.add(Box.createHorizontalStrut(1));

            add(box, BorderLayout.WEST);

            box = new Box(BoxLayout.X_AXIS);
            nextmon = new JButton();
            nextmon.setToolTipText(parent.getString("next.mon", "Next month."));
            icon = new ImageIcon(getImage("nextmon.gif"), ">");
            nextmon.setIcon(icon);
            nextmon.setPreferredSize(d);
            nextmon.addActionListener(this);
            box.add(nextmon);

            box.add(Box.createHorizontalStrut(1));

            nextyear = new JButton();
            nextyear.setToolTipText(parent.getString("next.year", "Next year."));
            icon = new ImageIcon(getImage("nextyear.gif"), ">>");
            nextyear.setIcon(icon);
            nextyear.setPreferredSize(d);
            nextyear.addActionListener(this);
            box.add(nextyear);

            add(box, BorderLayout.EAST);
            setCurrentMonth(parent.calendar_);
            // setLabel(parent.calendar);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Object src = e.getSource();
            final Calendar c = new GregorianCalendar(parent.getLocale());
            c.setTime(parent.getCalendar().getTime());

            if (src instanceof JButton) {
                if (e.getSource() == premon) {
                    c.add(Calendar.MONTH, -1);
                } else if (e.getSource() == nextmon) {
                    c.add(Calendar.MONTH, 1);
                } else if (e.getSource() == nextyear) {
                    c.add(Calendar.YEAR, 1);
                }
                if (e.getSource() == preyear) {
                    c.add(Calendar.YEAR, -1);
                }
                // System.out.println(c.getTime());
                parent.updateScreen(c);

            } else if (src instanceof JComboBox) {
                final JComboBox jcb = (JComboBox) src;
                if (src == monthBox) {
                    c.set(Calendar.MONTH, jcb.getSelectedIndex());
                } else if (e.getSource() == yearBox) {
                    c.set(Calendar.YEAR, years[jcb.getSelectedIndex()].intValue());
                    setYearComboBox(c);
                }
                parent.setMonthPanel(c);
                parent.screen.pack();
            }
        }

        public void setCurrentMonth(final Calendar c) {
            setMonthComboBox(c);
            setYearComboBox(c);

            if (box == null) {
                box = new Box(BoxLayout.X_AXIS);
                box.add(monthBox);
                box.add(Box.createHorizontalStrut(1));
                box.add(yearBox);
                box.add(Box.createHorizontalStrut(1));
                add(box, BorderLayout.CENTER);
            }

        }

        public void setLabel(final Calendar c) {
            if (lbl != null) {
                remove(lbl);
            }
            lbl = new JLabel(parent.getString("month." + c.get(Calendar.MONTH), "") + ", " + c.get(Calendar.YEAR));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            add(lbl, BorderLayout.CENTER);

        }

        private void setMonthComboBox(final Calendar c) {

            /* get the days from the calendar */
            final Map<String, Integer> monthIndexes = c.getDisplayNames(Calendar.MONTH, Calendar.SHORT,
                    parent.getLocale());
            final Map<Integer, String> idxMonths = new HashMap<Integer, String>();
            for (final String monthNm : monthIndexes.keySet()) {
                idxMonths.put(monthIndexes.get(monthNm), monthNm);
            }

            if (months == null) {
                months = new String[12];
                for (int i = 0; i < 12; i++) {
                    final String m = idxMonths.get(Integer.valueOf(i));
                    months[i] = m;
                }
            }
            if (monthBox == null) {
                monthBox = new JComboBox();
                monthBox.addActionListener(this);
                monthBox.setFont(DatePicker.PLAIN_FONT);
                monthBox.setSize(monthBox.getWidth(), height);
                monthBox.setPreferredSize(new Dimension(monthBox.getWidth(), height));
            }
            monthBox.setModel(new DefaultComboBoxModel(months));
            monthBox.setSelectedIndex(c.get(Calendar.MONTH));
        }

        private void setYearComboBox(final Calendar c) {
            final int y = c.get(Calendar.YEAR);
            years = new Integer[7];
            for (int i = y - 3, j = 0; i <= y + 3; i++, j++) {
                years[j] = new Integer(i);
            }
            if (yearBox == null) {
                yearBox = new JComboBox();
                yearBox.addActionListener(this);
                yearBox.setFont(DatePicker.PLAIN_FONT);
                yearBox.setSize(yearBox.getWidth(), height);
                yearBox.setPreferredSize(new Dimension(yearBox.getWidth(), height));
            }
            yearBox.setModel(new DefaultComboBoxModel(years));
            yearBox.setSelectedItem(years[3]);
        }

    }

    protected static Font PLAIN_FONT = new Font("Arial", Font.PLAIN, 10);

    protected static Font BOLD_FONT = new Font("Arial", Font.BOLD, 10);

    private MonthPanel monthPanel_;

    private NavigatePanel navPanel_;

    protected Calendar calendar_;

    private Calendar selectedDate_;

    private boolean closeOnSelect_ = true;

    private Locale locale_ = Locale.US;

    private DateFormat sdf_;

    private final JDialog screen;

    private ResourceBundle i18n;

    public DatePicker(final Window parent, final Observer observer) {
        this(parent, observer, new Date());
    }

    public DatePicker(final Window parent, final Observer observer, final Date selecteddate) {
        this(parent, observer, selecteddate, Locale.US);
    }

    public DatePicker(final Window parent, final Observer observer, final Date selecteddate, final Locale locale) {
        super();
        this.locale_ = locale;
        register(observer);
        screen = new JDialog(parent);
        screen.addWindowFocusListener(this);
        screen.setSize(200, 200);
        screen.setResizable(false);
        screen.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        screen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        screen.setTitle(getString("program.title", "Date Picker"));

        screen.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(final WindowEvent e) {
            }

            @Override
            public void windowLostFocus(final WindowEvent e) {
                final Window oppositeWin = e.getOppositeWindow();
                if (oppositeWin != null && SwingUtilities.isDescendingFrom(oppositeWin, screen)) {
                    return;
                }
//                
//                Will do nothing if clicked outside, so a date must be selected.
//                screen.dispose();
                
            }
        });
        

        final JPanel contentPane = (JPanel) screen.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // resolve the problem with the keys events...
        final ActionMap actionMap = contentPane.getActionMap();
        final InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "Escape");
        actionMap.put("Escape", new AbstractAction() {
            private static final long serialVersionUID = 1130993426922831973L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                screen.dispose();
            }
        });

        // create new calendar instance
        calendar_ = Calendar.getInstance(locale_);
        setSelectedDate(selecteddate);
        Calendar c = calendar_;
        if (selectedDate_ != null) {
            c = selectedDate_;
        }
        updateScreen(c);
        contentPane.add(navPanel_, BorderLayout.NORTH);
        contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black)));
    }

    public DatePicker(final Window parent, final Observer observer, final Locale locale) {
        this(parent, observer, new Date(), locale);
    }

    public String formatDate(final Calendar date) {
        if (date == null) {
            return "";
        }
        return formatDate(date.getTime());
    }

    public String formatDate(final Calendar date, final String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date.getTime());
    }

    public String formatDate(final Date date) {
        if (date == null) {
            return "";
        }
        if (sdf_ == null) {
            sdf_ = DateFormat.getDateInstance(DateFormat.LONG, locale_);
        }
        return sdf_.format(date);
    }

    public String formatDate(final Date date, final String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public Locale getLocale() {
        return locale_ == null ? Locale.getDefault() : locale_;
    }

    public String getString(final String key, final String dv) {
        if (i18n == null || getLocale() != i18n.getLocale()) {
            i18n = ResourceBundle.getBundle("com/scc/calendar/datepicker/i18n", getLocale());
        }
        final String val = i18n.getString(key);
        if (val == null) {
            return dv;
        } else {
            return val;
        }
    }

    public boolean isCloseOnSelect() {
        return closeOnSelect_;
    }

    public Date parseDate(final String date) {
        if (sdf_ == null) {
            sdf_ = DateFormat.getDateInstance(DateFormat.LONG, locale_);
        }
        try {
            return sdf_.parse(date);
        } catch (final Exception e) {
            return null;
        }
    }

    public void register(final Observer observer) {
        if (observer != null) {
            this.addObserver(observer);
        }
    }

    @Override
    public void run() {
        screen.pack();
        screen.setVisible(true);

    }

    public void setCloseOnSelect(final boolean closeOnSelect) {
        this.closeOnSelect_ = closeOnSelect;
    }

    public void setLocale(final Locale l) {
        this.locale_ = l;
    }

    public void setSelectedDate(final Date d) {
        if (d != null) {
            if (selectedDate_ == null) {
                selectedDate_ = new GregorianCalendar(locale_);
            }
            this.selectedDate_.setTime(d);
            updateScreen(selectedDate_);
        }
    }

    public void start(final Component c) {
        if (c != null) {
            final Point p = c.getLocationOnScreen();
            screen.setLocation(p.x - screen.getWidth() + c.getWidth(), p.y + c.getHeight());

        } else {
            final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            screen.setLocation((int) (dim.getWidth() - screen.getWidth()) / 2,
                    (int) (dim.getHeight() - screen.getHeight()) / 2);
        }
        SwingUtilities.invokeLater(this);
    }

    public void unregister(final Observer observer) {
        if (observer != null) {
            this.deleteObserver(observer);
        }
    }

    @Override
    public void windowGainedFocus(final WindowEvent e) {
    }

    @Override
    public void windowLostFocus(final WindowEvent e) {
        screen.toFront();
    }

    private Calendar getCalendar() {
        return calendar_;
    }

    protected void dayPicked(final int day) {
        // this.day = day;
        calendar_.set(Calendar.DAY_OF_MONTH, day);
        setSelectedDate(calendar_.getTime());
        this.setChanged();
        this.notifyObservers(selectedDate_);
        // System.out.println("cnt="+observable.countObservers()+", day
        // picked="+calendar_.getTime());
        if (closeOnSelect_) {
            screen.dispose();
            screen.setVisible(false);
        }
    }

    protected void setMonthPanel(final Calendar calendar) {
        if (calendar != null) {
            this.calendar_.setTime(calendar.getTime());
        }
        if (monthPanel_ != null) {
            screen.getContentPane().remove(monthPanel_);
        }
        monthPanel_ = new MonthPanel(this, calendar);
        screen.getContentPane().add(monthPanel_, BorderLayout.CENTER);
    }

    protected void updateScreen(final Calendar c) {
        if (navPanel_ == null) {
            navPanel_ = new NavigatePanel(this);
        }
        // navPanel_.setLabel(c);
        navPanel_.setCurrentMonth(c);
        setMonthPanel(c);
        screen.pack();
    }

}
