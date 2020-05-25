package application.data.service;

import application.data.entity.Order;
import application.data.repository.OrderRepository;
import application.model.viewmodel.chart.ChartDataVM1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    public void addNewOrder(Order order) {
        orderRepository.save(order);
    }

    public Order findOne(int orderId) {
        return orderRepository.findOne(orderId);
    }

    public List<Order> findOrderByGuidOrUserName(String guid, String userName) {
        return orderRepository.findOrderByGuidOrUserName(guid,userName);
    }

    public Page<Order> getListAllOrderOfShipper(Pageable pageable, String shipName) {
        return orderRepository.getListAllOrderOfShipper(pageable, shipName);
    }

    public List<ChartDataVM1> getTotalPriceInMonthOfYear2020() {
        return orderRepository.getTotalPriceInMonthOfYear2020();
    }

}
