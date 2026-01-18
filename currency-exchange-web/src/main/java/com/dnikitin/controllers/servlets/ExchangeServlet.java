package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.services.ExchangeService;
import com.dnikitin.util.HttpUtil;
import com.dnikitin.vo.CurrencyPair;
import com.dnikitin.dto.ExchangeValueDto;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * REST endpoint for currency conversion calculations.
 * Supports GET with query parameters 'from', 'to', and 'amount'.
 */
@WebServlet("/api/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService exchangeService;
    private JsonMapper jsonMapper;

    @Override
    public void init(){
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        exchangeService = context.getExchangeService();
        jsonMapper = context.getJsonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if(from == null || to == null || amount == null) {
            throw new InvalidParamsException("Missing required fields: from, to, amount");
        }

        HttpUtil.validateCodes(from, to);
        BigDecimal amountDecimal = HttpUtil.getBigDecimal(amount);

        ExchangeValueDto exchangeValue = exchangeService.exchange(new CurrencyPair(from, to), amountDecimal);

        jsonMapper.writeValue(resp.getWriter(), exchangeValue);
    }
}
