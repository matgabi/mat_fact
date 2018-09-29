package model.export;
import jxl.write.WriteException;

import java.io.IOException;

import java.util.List;

public interface ExcelExporter
{
    public void createExcel(List list) throws WriteException, IOException;
}
