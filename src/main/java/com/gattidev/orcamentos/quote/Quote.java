package com.gattidev.orcamentos.quote;

import com.gattidev.orcamentos.user.AppUser;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;

    @Column(nullable = false, length = 120)
    private String vehicle;

    @Column(name = "problem_description", nullable = false, length = 1000)
    private String problemDescription;

    @ElementCollection
    @CollectionTable(name = "quote_items", joinColumns = @JoinColumn(name = "quote_id"))
    private List<QuoteItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuoteStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Quote() {
    }

    public Quote(
            AppUser owner,
            String customerName,
            String vehicle,
            String problemDescription,
            List<QuoteItem> items
    ) {
        this.owner = owner;
        this.customerName = customerName;
        this.vehicle = vehicle;
        this.problemDescription = problemDescription;
        this.items = new ArrayList<>(items);
        this.status = QuoteStatus.DRAFT;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void update(String customerName, String vehicle, String problemDescription, List<QuoteItem> items) {
        this.customerName = customerName;
        this.vehicle = vehicle;
        this.problemDescription = problemDescription;
        this.items.clear();
        this.items.addAll(items);
        touch();
    }

    public void changeStatus(QuoteStatus status) {
        this.status = status;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public BigDecimal total() {
        return items.stream()
                .map(QuoteItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public AppUser getOwner() {
        return owner;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public List<QuoteItem> getItems() {
        return List.copyOf(items);
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
