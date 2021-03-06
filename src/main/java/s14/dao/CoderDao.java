package s14.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoderDao implements Dao<Coder> {
    private static final Logger log = LogManager.getLogger(CoderDao.class);

    private static final String GET_BY_PK = "SELECT coder_id, first_name, last_name, hire_date, salary FROM coders WHERE coder_id = ?";
    private static final String GET_ALL = "SELECT coder_id, first_name, last_name, hire_date, salary FROM coders";
    private static final String INSERT = "INSERT INTO coders(coder_id, first_name, last_name, hire_date, salary) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE coders SET first_name = ?, last_name = ?, hire_date = ?, salary = ? WHERE coder_id = ?";
    private static final String DELETE = "DELETE FROM coders WHERE coder_id = ?";

    @Override
    public List<Coder> getAll() {
        List<Coder> results = new ArrayList<>();

        try (Connection conn = Connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                Coder coder = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3),
                        rs.getObject(4, LocalDate.class), rs.getDouble(5));
                results.add(coder);
            }
        } catch (SQLException se) {
            log.error("Can't get all coders", se);
        }

        return results;
    }

    @Override
    public Optional<Coder> get(long id) {
        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coder my = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3),
                            rs.getObject(4, LocalDate.class), rs.getDouble(5));
                    return Optional.of(my);
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return Optional.empty();
    }

    public Coder legacyGet(long id) {
        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coder(rs.getLong(1), rs.getString(2), rs.getString(3),
                            rs.getObject(4, LocalDate.class), rs.getDouble(5));
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return null;
    }

    @Override
    public void save(Coder coder) {
        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setLong(1, coder.getId());
            ps.setString(2, coder.getFirstName());
            ps.setString(3, coder.getLastName());
            ps.setObject(4, coder.getHireDate());
            ps.setDouble(5, coder.getSalary());
            ps.executeUpdate();
        } catch (SQLException se) {
            log.error("Can't save coder " + coder.getId(), se);
        }
    }

    @Override
    public void update(Coder coder) {
        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, coder.getFirstName());
            ps.setString(2, coder.getLastName());
            ps.setObject(3, coder.getHireDate());
            ps.setDouble(4, coder.getSalary());
            ps.setLong(5, coder.getId());
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Updated " + count + " lines for " + coder);
            }
        } catch (SQLException se) {
            log.error("Can't update coder " + coder.getId(), se);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = Connector.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setLong(1, id);
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Deleted " + count + " lines for " + id);
            }
        } catch (SQLException se) {
            log.error("Can't delete coder " + id, se);
        }
    }
}
