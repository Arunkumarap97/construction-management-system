package com.ark.construction.dto;

public class ProjectReportDto {

    private Long projectId;
    private String projectName;
    private String clientName;
    private Double totalCost;
    private Double totalPaid;
    private Double totalExpense;
    private Double pending;
    private Double balance;
    private Double profit;

    public ProjectReportDto(Long projectId, String projectName, String clientName,
                            Double totalCost, Double totalPaid, Double totalExpense) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientName = clientName;
        this.totalCost = totalCost != null ? totalCost : 0.0;
        this.totalPaid = totalPaid != null ? totalPaid : 0.0;
        this.totalExpense = totalExpense != null ? totalExpense : 0.0;

        this.pending = this.totalCost - this.totalPaid;
        this.balance = this.totalPaid - this.totalExpense;
        this.profit = this.totalCost - this.totalExpense;
    }

    public Long getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getClientName() { return clientName; }
    public Double getTotalCost() { return totalCost; }
    public Double getTotalPaid() { return totalPaid; }
    public Double getTotalExpense() { return totalExpense; }
    public Double getPending() { return pending; }
    public Double getBalance() { return balance; }
    public Double getProfit() { return profit; }
}