package com.example.smart_healthcare_application.models;

import com.google.gson.annotations.SerializedName;

public class Prescription {

    @SerializedName("prescriptionId")
    private String prescriptionId;

    @SerializedName("medicationName")
    private String medicationName;

    @SerializedName("dosage")
    private String dosage;

    @SerializedName("frequency")
    private String frequency;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("prescribedDate")
    private String prescribedDate;

    // Constructor mặc định (cần thiết cho Firebase/Gson)
    public Prescription() {
    }

    public Prescription(String prescriptionId, String medicationName, String dosage, String frequency, int quantity, String instructions, String prescribedDate) {
        this.prescriptionId = prescriptionId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.quantity = quantity;
        this.instructions = instructions;
        this.prescribedDate = prescribedDate;
    }

    // --- Getters and Setters ---
    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getPrescribedDate() { return prescribedDate; }
    public void setPrescribedDate(String prescribedDate) { this.prescribedDate = prescribedDate; }
}