package com.alphawallet.app;

import com.alphawallet.app.dropdown.DropdownItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class SwapData {
    // List to hold all the chain information
    private List<JsonObject> chainList;
    public SwapData() {
        this.chainList = new ArrayList<>();
        JsonArray tokens_eth = new JsonArray();
        tokens_eth.add(createToken(false, R.drawable.ic_token_eth, "ETH", "", "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", 18));
        tokens_eth.add(createToken(false, R.drawable.ic_token_usdc, "USDC", "", "0xa0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6));
        tokens_eth.add(createToken(false, R.drawable.ic_token_usdt, "USDT", "https://static.coinall.ltd/cdn/web3/currency/token/1718088031349.png/type=png_350_0", "0xdac17f958d2ee523a2206206994597c13d831ec7", 6));
        tokens_eth.add(createToken(false,  R.drawable.ic_token_pepe,"PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-20230814.png", "0x6982508145454ce325ddbe47a25d4ec3d2311933", 18));
        tokens_eth.add(createToken(true, R.drawable.ic_token_usdc, "SHIB", "https://static.coinall.ltd/cdn/wallet/logo/SHIB-20220328.png", "0x95ad61b0a150d79219dcf64e1e6cc01f0b64c4ce", 18));
        tokens_eth.add(createToken(true, R.drawable.ic_token_ondo, "ONDO", "https://static.coinall.ltd/cdn/wallet/logo/ONDO-1679645167340.png", "0xfaba6f8e4a5e8ab82f62fe7c39859fa577269be3", 18));
        tokens_eth.add(createToken(false, R.drawable.ic_token_fet, "FET", "", "0xaea46a60368a7bd060eec7df8cba43b7ef41ad85", 18));
        this.addChain(
                tokens_eth,
                R.drawable.ic_token_eth,
                "Ethereum",
                "eth",
                "https://mainnet.infura.io/v3/1b5defa8ed65495c943a0dfa74fd832c",
                "0x68b5f967f06b36046707411e7888F6A0EBE86e3D",
                "0xf164fC0Ec4E93095b804a4795bBe1e041497b92a",
                1
        );
        JsonArray tokens_bsc = new JsonArray();
        tokens_bsc.add(createToken(false, R.drawable.ic_binance_logo, "BNB", "", "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c", 18));
        tokens_bsc.add(createToken(false, R.drawable.ic_token_usdc, "USDC", "", "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d", 18));
        tokens_bsc.add(createToken(false, R.drawable.ic_token_floki, "FLOKI", "", "0xfb5b838b6cfeedc2873ab27866079ac55363d37e", 9));
        tokens_bsc.add(createToken(true, R.drawable.ic_token_beam, "ATOM", "https://static.coinall.ltd/cdn/wallet/logo/ATOM-20220328.png", "0x0eb3a705fc54725037cc9e008bdede697f62f335", 18));
        tokens_bsc.add(createToken(true, R.drawable.ic_binance_logo, "PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-1690303247355.png", "0x25d887ce7a35172c62febfd67a1856f20faebb00", 18));
        tokens_bsc.add(createToken(true, R.drawable.ic_token_usdc, "DAI", "https://static.coinall.ltd/cdn/wallet/logo/dai01.png", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", 18));
        tokens_bsc.add(createToken(false, R.drawable.ic_token_ada, "ADA", "", "0x3ee2200efb3400fabb9aacf31297cbdd1d435d47", 18));
        this.addChain(
                tokens_bsc,
                R.drawable.ic_binance_logo,
                "Binace Smart Chain",
                "bsc",
                "https://bsc-dataseed.binance.org",
                "0x144A13c9e2B5481cFF90e7A1F819c937b0892933",
                "0x10ED43C718714eb63d5aA57B78B54704E256024E",
                56
        );
        JsonArray tokens_polygon = new JsonArray();
        tokens_polygon.add(createToken(false, R.drawable.ic_icons_polygon, "MATIC", "", "0x0d500B1d8E8eF31E21C99d1Db9A6444d3ADf1270", 18));
        tokens_polygon.add(createToken(false, R.drawable.ic_token_usdc, "USDC", "", "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359", 6));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_floki, "FLOKI", "", "0xfb5b838b6cfeedc2873ab27866079ac55363d37e", 9));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_beam, "ATOM", "https://static.coinall.ltd/cdn/wallet/logo/ATOM-20220328.png", "0x0eb3a705fc54725037cc9e008bdede697f62f335", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_binance_logo, "PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-1690303247355.png", "0x25d887ce7a35172c62febfd67a1856f20faebb00", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_usdc, "DAI", "https://static.coinall.ltd/cdn/wallet/logo/dai01.png", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", 18));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_ada, "ADA", "", "0x3ee2200efb3400fabb9aacf31297cbdd1d435d47", 18));
        this.addChain(
                tokens_polygon,
                R.drawable.ic_icons_polygon,
                "Polygon",
                "polygon",
                "https://polygon-rpc.com",
                "0x144A13c9e2B5481cFF90e7A1F819c937b0892933",
                "0xa5E0829CaCEd8fFDD4De3c43696c57F7D7A678ff",
                137
        );
        JsonArray tokens_ftm = new JsonArray();
        tokens_ftm.add(createToken(false, R.drawable.ic_fantom, "Fantom", "", "0x21be370D5312f44cB42ce377BC9b8a0cEF1A4C83", 18));
        tokens_ftm.add(createToken(false, R.drawable.ic_token_usdc, "USDC", "", "0x04068DA6C83AFCFA0e13ba15A6696662335D5B75", 6));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_floki, "FLOKI", "", "0xfb5b838b6cfeedc2873ab27866079ac55363d37e", 9));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_beam, "ATOM", "https://static.coinall.ltd/cdn/wallet/logo/ATOM-20220328.png", "0x0eb3a705fc54725037cc9e008bdede697f62f335", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_binance_logo, "PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-1690303247355.png", "0x25d887ce7a35172c62febfd67a1856f20faebb00", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_usdc, "DAI", "https://static.coinall.ltd/cdn/wallet/logo/dai01.png", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", 18));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_ada, "ADA", "", "0x3ee2200efb3400fabb9aacf31297cbdd1d435d47", 18));
        this.addChain(
                tokens_ftm,
                R.drawable.ic_fantom,
                "Fantom",
                "ftm",
                "https://rpc.ftm.tools/",
                "0x144A13c9e2B5481cFF90e7A1F819c937b0892933",
                "0xF491e7B69E4244ad4002BC14e878a34207E38c29",
                250
        );
        JsonArray tokens_arb = new JsonArray();
        tokens_arb.add(createToken(false, R.drawable.ic_icons_arbitrum, "ETH", "", "0x82aF49447D8a07e3bd95BD0d56f35241523fBab1", 18));
        tokens_arb.add(createToken(false, R.drawable.ic_token_usdc, "USDC", "", "0xaf88d065e77c8cC2239327C5EDb3A432268e5831", 6));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_floki, "FLOKI", "", "0xfb5b838b6cfeedc2873ab27866079ac55363d37e", 9));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_beam, "ATOM", "https://static.coinall.ltd/cdn/wallet/logo/ATOM-20220328.png", "0x0eb3a705fc54725037cc9e008bdede697f62f335", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_binance_logo, "PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-1690303247355.png", "0x25d887ce7a35172c62febfd67a1856f20faebb00", 18));
//        tokens_bsc.add(createToken(true, R.drawable.ic_token_usdc, "DAI", "https://static.coinall.ltd/cdn/wallet/logo/dai01.png", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", 18));
//        tokens_bsc.add(createToken(false, R.drawable.ic_token_ada, "ADA", "", "0x3ee2200efb3400fabb9aacf31297cbdd1d435d47", 18));
        this.addChain(
                tokens_arb,
                R.drawable.ic_icons_arbitrum,
                "Arbitrum",
                "arbitrum",
                "https://arb1.arbitrum.io/rpc",
                "0x144A13c9e2B5481cFF90e7A1F819c937b0892933",
                "0x1b02dA8Cb0d097eB8D57A175b88c7D8b47997506",
                42161
        );
    }
    // Method to create a token JsonObject
    public JsonObject createToken(boolean isSearched, int icon, String text, String imgUrl, String address, int decimal) {
        JsonObject token = new JsonObject();
        token.addProperty("isSearched", isSearched);
        token.addProperty("icon", icon);
        token.addProperty("text", text);
        token.addProperty("imgUrl", imgUrl);
        token.addProperty("address", address);
        token.addProperty("decimal", decimal);
        return token;
    }
    // Method to add a new chain to the list
    public void addChain(JsonArray tokens, int icon, String chianName, String shortName, String rpcURL, String swapContractAddress, String swapRouterAddress, int chainId) {
        JsonObject chainInfo = new JsonObject();
        chainInfo.add("tokens", tokens);
        chainInfo.addProperty("icon", icon);
        chainInfo.addProperty("rpcURL", rpcURL);
        chainInfo.addProperty("chainName", chianName);
        chainInfo.addProperty("shortName", shortName);
        chainInfo.addProperty("swapContractAddress", swapContractAddress);
        chainInfo.addProperty("swapRouterAddress", swapRouterAddress);
        chainInfo.addProperty("chainId", chainId);
        this.chainList.add(chainInfo);
    }
    // Method to remove a chain by index
    public void removeChain(int index) {
        if (index >= 0 && index < chainList.size()) {
            chainList.remove(index);
        } else {
            System.out.println("Index out of bounds.");
        }
    }
    // Method to get a chain by index
    public JsonObject getChain(int index) {
        if (index >= 0 && index < chainList.size()) {
            return chainList.get(index);
        } else {
            System.out.println("Index out of bounds.");
            return null;
        }
    }
    // Method to update a chain by index
    public void updateChain(int index, JsonArray tokens, String rpcURL, String chianName, String shortName, String swapContractAddress, String swapRouterAddress, int chainId) {
        if (index >= 0 && index < chainList.size()) {
            JsonObject chainInfo = new JsonObject();
            chainInfo.add("tokens", tokens);
            chainInfo.addProperty("rpcURL", rpcURL);
            chainInfo.addProperty("chainName", chianName);
            chainInfo.addProperty("shortName", shortName);
            chainInfo.addProperty("swapContractAddress", swapContractAddress);
            chainInfo.addProperty("swapRouterAddress", swapRouterAddress);
            chainInfo.addProperty("chainId", chainId);
            chainList.set(index, chainInfo);
        } else {
            System.out.println("Index out of bounds.");
        }
    }
    // Method to convert the entire chain list to a JSON array
    public JsonArray toJsonArray() {
        JsonArray jsonArray = new JsonArray();
        for (JsonObject chainInfo : chainList) {
            jsonArray.add(chainInfo);
        }
        return jsonArray;
    }
    // Method to get the number of chains in the list
    public int getSize() {
        return chainList.size();
    }
    // Example usage
}
