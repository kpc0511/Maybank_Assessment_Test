package com.maybank.platform.services.restapi.vo;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Measurement {
    @CsvBindByName(column = "TIME")
    private String time;
    @CsvBindByName(column = "PM1125H_1 > I1")
    private String pm1125h1I1;
    @CsvBindByName(column = "PM1125H_1 > I2")
    private String pm1125h1I2;
    @CsvBindByName(column = "PM1125H_1 > I3")
    private String pm1125h1I3;
    @CsvBindByName(column = "PM1125H_1 > V1")
    private String pm1125h1V1;
    @CsvBindByName(column = "PM1125H_1 > V2")
    private String pm1125h1V2;
    @CsvBindByName(column = "PM1125H_1 > V3")
    private String pm1125h1V3;
    @CsvBindByName(column = "PM1125H_1 > P1")
    private String pm1125h1P1;
    @CsvBindByName(column = "PM1125H_1 > P2")
    private String pm1125h1P2;
    @CsvBindByName(column = "PM1125H_1 > P3")
    private String pm1125h1P3;
    @CsvBindByName(column = "PM1125H_1 > P_total")
    private String pm1125h1PTotal;
    @CsvBindByName(column = "PM1125H_1 > Q_total")
    private String pm1125h1QTotal;
    @CsvBindByName(column = "PM1125H_1 > Energy_delivered")
    private String pm1125h1EnergyDelivered;
    @CsvBindByName(column = "DELAB_2 > I1")
    private String delab2I1;
    @CsvBindByName(column = "DELAB_2 > I2")
    private String delab2I2;
    @CsvBindByName(column = "DELAB_2 > I3")
    private String delab2I3;
    @CsvBindByName(column = "DELAB_2 > V1")
    private String delab2V1;
    @CsvBindByName(column = "DELAB_2 > V2")
    private String delab2V2;
    @CsvBindByName(column = "DELAB_2 > V3")
    private String delab2V3;
    @CsvBindByName(column = "DELAB_2 > Q1")
    private String delab2Q1;
    @CsvBindByName(column = "DELAB_2 > P2")
    private String delab2P2;
    @CsvBindByName(column = "DELAB_2 > P3")
    private String delab2P3;
    @CsvBindByName(column = "DELAB_2 > Energy_export")
    private String delab2EnergyExport;
    @CsvBindByName(column = "DELAB_2 > Energy_import")
    private String delab2EnergyImport;
    @CsvBindByName(column = "DELAB_2 > P1")
    private String delab2P1;
    @CsvBindByName(column = "DELAB_2 > Q2")
    private String delab2Q2;
    @CsvBindByName(column = "DELAB_2 > Q3")
    private String delab2Q3;
    @CsvBindByName(column = "DELAB_3 > I1")
    private String delab3I1;
    @CsvBindByName(column = "DELAB_3 > I2")
    private String delab3I2;
    @CsvBindByName(column = "DELAB_3 > I3")
    private String delab3I3;
    @CsvBindByName(column = "DELAB_3 > V1")
    private String delab3V1;
    @CsvBindByName(column = "DELAB_3 > V2")
    private String delab3V2;
    @CsvBindByName(column = "DELAB_3 > V3")
    private String delab3V3;
    @CsvBindByName(column = "DELAB_3 > Energy_export")
    private String delab3EnergyExport;
    @CsvBindByName(column = "DELAB_3 > Energy_import")
    private String delab3EnergyImport;
}
