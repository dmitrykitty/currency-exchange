package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.exceptions.InvalidCurrencyException;
import com.dnikitin.services.ExchangeService;
import com.dnikitin.vo.CurrencyPair;
import com.dnikitin.dto.ExchangeValue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService;
    private JsonMapper jsonMapper;

    @Override
    public void init() throws ServletException {
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        exchangeService = context.getExchangeService();
        jsonMapper = context.getJsonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if(from == null || to == null || amount == null) {
            throw new InvalidCurrencyException("Missing required fields: from, to, amount");
        }

        BigDecimal amountDecimal = new BigDecimal(amount);
        ExchangeValue exchangeValue = exchangeService.exchange(new CurrencyPair(from, to), amountDecimal);

        jsonMapper.writeValue(resp.getWriter(), exchangeValue);
    }
}
