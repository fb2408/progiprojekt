package hr.fer.proinz.proggers.parkshare.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "client")
@Entity
public class Client {
    @Id
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Column(name = "walletbalance", nullable = false, precision = 1000, scale = 2)
    private BigDecimal walletBalance;

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "walletBalance = " + walletBalance + ")";
    }

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client", optional = false)
//    private User user;
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}