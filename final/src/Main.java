import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException {
        List<Sale> sales;
        try {
            sales = parseSalesXML("src/data/sales.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TopSellers(sales);
        TopDates(sales);
    }
    private static void TopSellers (List<Sale> sales) throws ParserConfigurationException {
        try {
        // Map для хранения количества проданных товаров по продавцам
        Map<Integer, Integer> sellerSales = new HashMap<>();
        for (Sale sale : sales) {
            sellerSales.merge(sale.getSellerId(), sale.getQuantity(), Integer::sum);
        }
        // Сортируем продавцов по количеству проданных товаров
        List<Map.Entry<Integer, Integer>> topSellers = new ArrayList<>(sellerSales.entrySet());
        topSellers.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());
        topSellers = topSellers.subList(0, Math.min(topSellers.size(), 5)); // Ограничиваем список топ 5 продавцами

        // Создаем XML документ для записи информации о топ 5 продавцах
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Создаем корневой элемент top_sellers
        Element rootElement = doc.createElement("top_sellers");
        doc.appendChild(rootElement);

        // Добавляем информацию о каждом продавце
        for (Map.Entry<Integer, Integer> entry : topSellers) {
            int sellerId = entry.getKey();
            int quantitySold = entry.getValue();

            Element seller = doc.createElement("seller");
            rootElement.appendChild(seller);

            Element id = doc.createElement("seller_id");
            id.appendChild(doc.createTextNode(String.valueOf(sellerId)));
            seller.appendChild(id);

            Element quantity = doc.createElement("quantity_sold");
            quantity.appendChild(doc.createTextNode(String.valueOf(quantitySold)));
            seller.appendChild(quantity);
        }

        // Записываем содержимое в XML файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/data/top_sellers.xml"));
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        System.out.println("XML файл top_sellers успешно создан!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void TopDates (List<Sale> sales) throws ParserConfigurationException {
        // Map для хранения количества проданных товаров по датам
        Map<String, Integer> dateSales = new HashMap<>();
        for (Sale sale : sales) {
            dateSales.merge(sale.getDate(), sale.getQuantity(), Integer::sum);
        }

        // Сортируем даты по количеству проданных товаров
        List<Map.Entry<String, Integer>> topDates = new ArrayList<>(dateSales.entrySet());
        topDates.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        topDates = topDates.subList(0, Math.min(topDates.size(), 5)); // Ограничиваем список топ 5 дат

        // Создаем XML файл и записываем данные о топ 5 датах
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Создаем корневой элемент top_dates
        Element rootElement = doc.createElement("top_dates");
        doc.appendChild(rootElement);

        // Добавляем информацию о каждой дате
        for (Map.Entry<String, Integer> entry : topDates) {
            String date = entry.getKey();
            int quantitySold = entry.getValue();

            Element dateElement = doc.createElement("dates");
            rootElement.appendChild(dateElement);

            Element dateNode = doc.createElement("date");
            dateNode.appendChild(doc.createTextNode(date));
            dateElement.appendChild(dateNode);


            Element quantityNode = doc.createElement("quantity_sold");
            quantityNode.appendChild(doc.createTextNode(String.valueOf(quantitySold)));
            dateElement.appendChild(quantityNode);
        }

        // Записываем содержимое в XML файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/data/top_dates.xml"));
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("XML файл top_dates успешно создан!");
    }

    private static List<Sale> parseSalesXML(String filename) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(filename));
        doc.getDocumentElement().normalize();

        List<Sale> sales = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("sale");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            int id = Integer.parseInt(element.getElementsByTagName("sale_id").item(0).getTextContent());
            int sellerId = Integer.parseInt(element.getElementsByTagName("seller_id").item(0).getTextContent());
            int productId = Integer.parseInt(element.getElementsByTagName("product_id").item(0).getTextContent());
            int quantity = Integer.parseInt(element.getElementsByTagName("quantity").item(0).getTextContent());
            String date=element.getElementsByTagName("date").item(0).getTextContent();

            Sale sale = new Sale(id, sellerId, productId, quantity, date);
            sales.add(sale);
        }

        return sales;
    }
}
