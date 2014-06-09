package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Morana Nika
 */
public class ImportResult {

    private String errors = "";

    private Integer importedGoodsCount = 0;
    private Integer importedSuppliersCount = 0;

    public ImportResult(Integer goodsCount, Integer suppliersCount, String _errors) {
        errors = _errors;
        importedGoodsCount = goodsCount;
        importedSuppliersCount = suppliersCount;
    }

    public ImportResult() {
    }

    public String getErrors() {
        return errors;
    }

    public void addError(String err) {
        errors += err;
    }

    public Integer getImportedGoodsCount() {
        return importedGoodsCount;
    }

    public void setImportedGoodsCount(Integer i) {
        importedGoodsCount = i;
    }

    public Integer getImportedSuppliersCount() {
        return importedSuppliersCount;
    }

    public void setImportedSuppliersCount(Integer i) {
        importedSuppliersCount = i;
    }

}
