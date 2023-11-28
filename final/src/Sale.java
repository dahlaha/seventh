public class Sale {
    private int saleId;
    private int sellerId;
    private int productId;
    private int quantity;
    private String date;

    public Sale(int saleId, int sellerId, int productId, int quantity,String date) {
        this.saleId = saleId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.quantity = quantity;
        this.date=date;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", sellerId=" + sellerId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", date='" + date + '\'' +
                '}';
    }

}
