package com.scc.widgets;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.scc.calendar.datepicker.DatePicker;

public class TestDayPicker {

    public static void main(final String argv[]) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                   
                    doStart(argv);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
//        doStart(argv);
    }

    public static void doStart(String argv[]) {
        System.out.println("Usage: java -jar DatePacker.jar {ISO Language Code, e.g. de}");
        final JFrame frame = new JFrame("Test Date Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        final Container contentPane = frame.getContentPane();
        contentPane.setLayout(new FlowLayout());

        // create a text-field that implements Observer
        final ObservingTextField textField = new ObservingTextField();
        textField.setEditable(false);
        textField.setColumns(20);
        textField.setText("");
        contentPane.add(textField);

        // create a button
        String lang = null;
        if (argv.length > 0)
            lang = argv[0];
        final Locale locale = getLocale(lang);
        final JButton btn = new JButton("...");
        Dimension d = btn.getPreferredSize();
        d.height = textField.getPreferredSize().height;
        d.width = d.height;
        btn.setPreferredSize(d);
        contentPane.add(btn);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // instantiate the DatePicker
                DatePicker dp = new DatePicker(frame, textField, locale);
                // previously selected date
                Date selectedDate = dp.parseDate(textField.getText());
                dp.setSelectedDate(selectedDate);
                dp.start(btn);
            };
        });
        frame.setVisible(true);
    }

    private static Locale getLocale(String loc) {
        if (loc != null && loc.length() > 0)
            return new Locale(loc);
        else
            return Locale.US;
    }
}

class ObservingTextField extends JTextField implements Observer {
    private static final long serialVersionUID = -9121215994812342536L;

    public void update(Observable o, Object arg) {
        DatePicker dp = (DatePicker) o;
        Calendar calendar = (Calendar) arg;
        System.out.println("picked=" + dp.formatDate(calendar));
        setText(dp.formatDate(calendar));
    }
}
