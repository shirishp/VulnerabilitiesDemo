package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ContactsController {

    @RequestMapping("/contacts")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "Shirish") String name, Model model) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:file:~/testdb", "sa", "sa");

        Statement stmt = conn.createStatement();

        String sql = "select * from contacts where name = '" + name + "'";

        ResultSet rs = stmt.executeQuery(sql);

        List<Contact> contacts = new ArrayList<>();


        while (rs.next()) {
            contacts.add(new Contact(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        conn.close();

        model.addAttribute("sql", sql);
        model.addAttribute("contacts", contacts);
        return "contacts";
    }

    @RequestMapping("/contactSecure")
    public String greetingSecure(@RequestParam(value = "name", required = false, defaultValue = "Shirish") String name, Model model) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:file:~/testdb", "sa", "sa");

        PreparedStatement sql = conn.prepareStatement
                ("select * from contacts where name = ?");

        sql.setString(1, name);

        ResultSet rs = sql.executeQuery();

        List<Contact> contacts = new ArrayList<>();


        while (rs.next()) {
            contacts.add(new Contact(rs.getString(1), rs.getString(2), rs.getString(3)));
        }

        conn.close();

        model.addAttribute("sql", sql.toString());
        model.addAttribute("contacts", contacts);
        return "contacts";
    }

}
