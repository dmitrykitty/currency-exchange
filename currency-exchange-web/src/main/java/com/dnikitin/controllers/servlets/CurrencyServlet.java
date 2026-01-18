package com.dnikitin.controllers.servlets;

import com.dnikitin.controllers.AppContext;
import com.dnikitin.dto.CurrencyDto;
import com.dnikitin.exceptions.InvalidParamsException;
import com.dnikitin.services.CurrencyService;

import com.dnikitin.util.HttpUtil;
import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * REST endpoint for individual currency data.
 * Supports GET for retrieving a specific currency by its code in the path (e.g., /currency/USD).
 */
@WebServlet("/api/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;
    private JsonMapper jsonMapper;

    @Override
    public void init() {
        AppContext context = (AppContext) getServletContext().getAttribute(AppContext.class.getSimpleName());

        currencyService = context.getCurrencyService();
        jsonMapper = context.getJsonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new InvalidParamsException("Missing currency code");
        }

        pathInfo = pathInfo.substring(1); //remove /
        HttpUtil.validateCode(pathInfo);

        CurrencyDto currencyByCode = currencyService.getCurrencyByCode(pathInfo);
        jsonMapper.writeValue(resp.getWriter(), currencyByCode);
    }
}
