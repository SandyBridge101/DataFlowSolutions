module DataFlowSolutions {
    requires javafx.controls;
    requires javafx.graphics;
    //requires org.apache.poi.poi;
    requires poi.ooxml;
    requires org.apache.commons.csv;
    requires org.apache.pdfbox;
    //requires org.apache.poi.scratchpad;
    requires com.opencsv;
    requires java.logging;
    requires junit;

    exports test;

    opens ui;
}