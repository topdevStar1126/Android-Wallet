package com.alphawallet.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.math.BigDecimal;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alphawallet.app.dropdown.ChainDropdownAdapter;
import com.alphawallet.app.dropdown.ChainDropdownItem;
import com.alphawallet.app.dropdown.CustomDropdownAdapter;
import com.alphawallet.app.dropdown.DropdownItem;
import com.alphawallet.app.swapContractOnEth.SwapContractOnETH;
import com.alphawallet.app.swapContractOnbsc.SwapContractOnBSC;
import com.alphawallet.app.ui.BaseFragment;
import com.alphawallet.app.ui.SwapSlippageSettingDialog;
import com.alphawallet.app.viewmodel.HomeViewModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import timber.log.Timber;


public class SwapFragment extends BaseFragment {
    HomeViewModel viewModel;
    private SwapData chainData;
    private String currentWalletAddress;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Web3j web3;
    private BigDecimal gasUsdValue;
    SwapContractOnBSC swapContractOnBSC;
    SwapContractOnETH swapContractOnETH;
    private BigDecimal currentGasPrice;
    SharedPreferencesManager sharedPreferencesManager;
    private final static String RPC_URL_ETH = "https://mainnet.infura.io/v3/1b5defa8ed65495c943a0dfa74fd832c";
    private final static String RPC_URL_BSC = "https://bsc-dataseed.binance.org";
    private final static String ETH_ADDRESS = "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2";
    private final static String BNB_ADDRESS = "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
    private final static String MATIC_ADDRESS = "0x0d500B1d8E8eF31E21C99d1Db9A6444d3ADf1270";
    private final static String uniSwapV2Router_address = "0xf164fC0Ec4E93095b804a4795bBe1e041497b92a";
    private final static String pancakeSwapV2Router_address = "0x10ED43C718714eb63d5aA57B78B54704E256024E";
    private final static String quickSwapV2Router_address = "0xa5E0829CaCEd8fFDD4De3c43696c57F7D7A678ff";
    private final static String swapContractAddressOnBsc = "0x144A13c9e2B5481cFF90e7A1F819c937b0892933";
    private final static String swapContractAddressOnETH = "0x68b5f967f06b36046707411e7888F6A0EBE86e3D";

    private ProgressBar spinner;
    private String privateKey;
    TextView textviewForReceiveToken, textviewForTransferToken, predictValueLabel, fromBalanceLabel, toBalanceLabel, timerLabel;
    Dialog dialogForReceiveToken, dialogForTransferToken;
    ImageView originTokenImg, changedTokenImg;

    FloatingActionButton exchangeBtn;
    private Timer timer;

    Spinner chain_spinner;
    List<DropdownItem> items_eth, items_bnb, items;
    ArrayList<CustomDropdownAdapter> receive_adapter, transfer_adapter;
    EditText editTextForReceiveToken, editTextForTransferToken, amountEdit;
    ListView listViewForReceiveToken, listViewForTransferToken;
    int selected_chainId = 1;
    int selected_position = 0;
    int selected_transfer_position = 0, selected_receive_position = 1;
    BigDecimal amountToSwap = new BigDecimal("0");
    BigDecimal amountToSwapInSmallUnit = new BigDecimal(0);
    BigDecimal amountToOut = new BigDecimal(0);

    BigDecimal fromTokenBalance = BigDecimal.valueOf(0), toTokenBalance = BigDecimal.valueOf(0);

    private RequestQueue requestQueue;

    private int timerValue;
    private TextView resultView;
    private TimerTask timerTask;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isFetching, isSwaping;

    public SwapFragment() {
    }

    public static SwapFragment newInstance(String param1, String param2) {
        SwapFragment fragment = new SwapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(HomeViewModel.class);

        viewModel.identify();
        viewModel.setWalletStartup();
        viewModel.setCurrencyAndLocale(requireContext());
        viewModel.tryToShowWhatsNewDialog(requireContext());

        currentWalletAddress = viewModel.getWalletFullName(requireContext());
        sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext());
        privateKey = sharedPreferencesManager.getString(currentWalletAddress, "default");

    }

    @SuppressLint({"MissingInflatedId", "ResourceAsColor", "WrongThread"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        timerValue = 16;
        isFetching = false;
        isSwaping = false;

        View view = inflater.inflate(R.layout.fragment_swap, container, false);

        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);
        resultView = (TextView) view.findViewById(R.id.resultView);

        chainData = new SwapData();
        chain_spinner = view.findViewById(R.id.chain_spinner);

        List<ChainDropdownItem> chain_items = new ArrayList<>();
        for (int i = 0; i < chainData.getSize(); i++) {
            chain_items.add(new ChainDropdownItem(
                    chainData.getChain(i).get("icon").getAsInt(),
                    chainData.getChain(i).get("chainName").getAsString()
            ));
        }
        ChainDropdownAdapter chain_adapter =
                new ChainDropdownAdapter(requireContext(), R.layout.token_dropdown_item, chain_items);
        chain_spinner.setAdapter(chain_adapter);

        requestQueue = Volley.newRequestQueue(requireContext());

        MaterialButton confirmBtn = view.findViewById(R.id.confirmBtn);
        MaterialButton maxBtn = view.findViewById(R.id.maxBtn);
        MaterialButton rate1Btn = view.findViewById(R.id.rate1Btn);
        MaterialButton rate2Btn = view.findViewById(R.id.rate2Btn);
        MaterialButton rate3Btn = view.findViewById(R.id.rate3Btn);

        originTokenImg = view.findViewById(R.id.token_img);
        changedTokenImg = view.findViewById(R.id.to_token_img);
        exchangeBtn = view.findViewById(R.id.exchangeBtn);
        textviewForReceiveToken = view.findViewById(R.id.textviewForReceiveToken);
        textviewForTransferToken = view.findViewById(R.id.textviewForTransferToken);
        predictValueLabel = view.findViewById(R.id.predictLabel);
        amountEdit = view.findViewById(R.id.amountEdit);
        fromBalanceLabel = view.findViewById(R.id.fromBalanceLabel);
        toBalanceLabel = view.findViewById(R.id.toBalanceLabel);

        timerLabel = view.findViewById(R.id.timerLabel);
        transfer_adapter = new ArrayList<>();
        receive_adapter = new ArrayList<>();

        for (int i = 0; i < chainData.getSize(); i++) {
            items = new ArrayList<>();
            JsonArray tokens = chainData.getChain(i).get("tokens").getAsJsonArray();
            Timber.e("token count: %s", tokens.size());
            for (int j = 0; j < tokens.size(); j++) {
                JsonObject token = tokens.get(j).getAsJsonObject();
                items.add(new DropdownItem(
                        token.get("isSearched").getAsBoolean(),
                        token.get("icon").getAsInt(),
                        token.get("text").getAsString(),
                        token.get("imgUrl").getAsString(),
                        token.get("address").getAsString(),
                        token.get("decimal").getAsInt()
                ));
            }
            transfer_adapter.add(new CustomDropdownAdapter(requireContext(), R.layout.token_dropdown_item, items));
            receive_adapter.add(new CustomDropdownAdapter(requireContext(), R.layout.token_dropdown_item, items));
        }
//        items_eth = new ArrayList<>();
//        items_eth.add(new DropdownItem(false, R.drawable.ic_token_eth, "ETH", "", ETH_ADDRESS, 18));
//        items_eth.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0xa0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6));
//        items_eth.add(new DropdownItem(false, R.drawable.ic_token_usdt, "USDT", "https://static.coinall.ltd/cdn/web3/currency/token/1718088031349.png/type=png_350_0", "0xdac17f958d2ee523a2206206994597c13d831ec7", 6));
//        items_eth.add(new DropdownItem(false,  R.drawable.ic_token_pepe,"PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-20230814.png", "0x6982508145454ce325ddbe47a25d4ec3d2311933", 18));
//        items_eth.add(new DropdownItem(true, R.drawable.ic_token_usdc, "SHIB", "https://static.coinall.ltd/cdn/wallet/logo/SHIB-20220328.png", "0x95ad61b0a150d79219dcf64e1e6cc01f0b64c4ce", 18));
//        items_eth.add(new DropdownItem(true, R.drawable.ic_token_ondo, "ONDO", "https://static.coinall.ltd/cdn/wallet/logo/ONDO-1679645167340.png", "0xfaba6f8e4a5e8ab82f62fe7c39859fa577269be3", 18));
//        items_eth.add(new DropdownItem(false, R.drawable.ic_token_fet, "FET", "", "0xaea46a60368a7bd060eec7df8cba43b7ef41ad85", 18));
////        items_eth.add(new DropdownItem(true, R.drawable.ic_token_eth, "ETH", "", "0x6e79b51959cf968d87826592f46f819f92466615", 18));
////        items_eth.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0xa0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6));
////        items_eth.add(new DropdownItem(true, R.drawable.ic_token_eth, "ETH", "", ETH_ADDRESS, 18));
////        items_eth.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0xa0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6));
////        items_eth.add(new DropdownItem(true, R.drawable.ic_token_eth, "ETH", "", ETH_ADDRESS, 18));
////        items_eth.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0xa0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6));
//
//
//        items_bnb = new ArrayList<>();
//        items_bnb.add(new DropdownItem(false, R.drawable.ic_binance_logo, "BNB", "", BNB_ADDRESS, 18));
//        items_bnb.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d", 18));
//        items_bnb.add(new DropdownItem(false, R.drawable.ic_token_floki, "FLOKI", "", "0xfb5b838b6cfeedc2873ab27866079ac55363d37e", 9));
//        items_bnb.add(new DropdownItem(true, R.drawable.ic_token_beam, "ATOM", "https://static.coinall.ltd/cdn/wallet/logo/ATOM-20220328.png", "0x0eb3a705fc54725037cc9e008bdede697f62f335", 18));
//        items_bnb.add(new DropdownItem(true, R.drawable.ic_binance_logo, "PEPE", "https://static.coinall.ltd/cdn/wallet/logo/PEPE-1690303247355.png", "0x25d887ce7a35172c62febfd67a1856f20faebb00", 18));
//        items_bnb.add(new DropdownItem(true, R.drawable.ic_token_usdc, "DAI", "https://static.coinall.ltd/cdn/wallet/logo/dai01.png", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", 18));
//        items_bnb.add(new DropdownItem(false, R.drawable.ic_token_ada, "ADA", "", "0x3ee2200efb3400fabb9aacf31297cbdd1d435d47", 18));
//        //items_bnb.add(new DropdownItem(false, R.drawable.ic_token_usdc, "USDC", "", "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d", 18));
        dialogForReceiveToken=new Dialog(requireContext());
        dialogForReceiveToken.setContentView(R.layout.dialog_searchable_receive);
        Objects.requireNonNull(dialogForReceiveToken.getWindow()).setLayout(650,800);
        dialogForReceiveToken.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editTextForReceiveToken = dialogForReceiveToken.findViewById(R.id.edit_text_search_receive);
        listViewForReceiveToken = dialogForReceiveToken.findViewById(R.id.list_view_receive_tokens);
        editTextForReceiveToken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                receive_adapter.get(selected_position).getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (isValidAddress(input)) {
                    fetchTokenInfo(input, true);
                }
            }
        });
        dialogForTransferToken = new Dialog(requireContext());
        dialogForTransferToken.setContentView(R.layout.dialog_searchable_transfer);
        Objects.requireNonNull(dialogForTransferToken.getWindow()).setLayout(650,800);
        dialogForTransferToken.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editTextForTransferToken = dialogForTransferToken.findViewById(R.id.edit_text_search_transfer);
        listViewForTransferToken = dialogForTransferToken.findViewById(R.id.list_view_transfer_tokens);
        editTextForTransferToken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                transfer_adapter.get(selected_position).getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (isValidAddress(input)) {
                    fetchTokenInfo(input, true);
                }
            }
        });

        listViewForTransferToken.setAdapter(transfer_adapter.get(selected_position));
        listViewForTransferToken.setOnItemClickListener((parent, view13, position, id) -> {
            DropdownItem selectedItem;
            selectedItem = transfer_adapter.get(selected_position).getItem(position);
            textviewForTransferToken.setText(Objects.requireNonNull(selectedItem).getText());
            if(selectedItem.getIsSearched()) {
                if(selectedItem.getImageUrl().isEmpty()) {
                    originTokenImg.setImageResource(selectedItem.getImageResId());
                } else {
                    Glide.with(requireContext())
                            .load(selectedItem.getImageUrl())
                            .into(originTokenImg);
                }
            } else {
                originTokenImg.setImageResource(selectedItem.getImageResId());
            }

            dialogForTransferToken.dismiss();
            selected_transfer_position = position;
            updateDataForSwap();
        });
        textviewForTransferToken.setOnClickListener(v -> dialogForTransferToken.show());
        originTokenImg.setOnClickListener(v -> dialogForTransferToken.show());

        listViewForReceiveToken.setAdapter(receive_adapter.get(selected_position));
        listViewForReceiveToken.setOnItemClickListener((parent, view12, position, id) -> {
            DropdownItem selectedItem;
            selectedItem = receive_adapter.get(selected_position).getItem(position);
            textviewForReceiveToken.setText(Objects.requireNonNull(selectedItem).getText());
            if(selectedItem.getIsSearched()) {
                if(selectedItem.getImageUrl().isEmpty()) {
                    changedTokenImg.setImageResource(selectedItem.getImageResId());
                } else {
                    Glide.with(requireContext())
                            .load(selectedItem.getImageUrl())
                            .into(changedTokenImg);
                }
            } else {
                changedTokenImg.setImageResource(selectedItem.getImageResId());
            }

            dialogForReceiveToken.dismiss();
            selected_receive_position = position;
            updateDataForSwap();
        });
        textviewForReceiveToken.setOnClickListener(v -> dialogForReceiveToken.show());
        changedTokenImg.setOnClickListener(v -> dialogForReceiveToken.show());


        maxBtn.setOnClickListener(view1 -> {
            calculatePercentAmount("max");
        });

        rate1Btn.setOnClickListener(view1 -> {
            calculatePercentAmount("0.75");
        });

        rate2Btn.setOnClickListener(view1 -> {
            calculatePercentAmount("0.5");
        });

        rate3Btn.setOnClickListener(view1 -> {
            calculatePercentAmount("0.25");
        });

        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().isEmpty()) {
                    confirmBtn.setEnabled(false);
                    amountEdit.setText("0");
                    amountToSwap = BigDecimal.valueOf(0);
                    return;
                }
                if(!isDecimal(s.toString())) {
                    amountEdit.setText(amountToSwap.toString());
                    return;
                }
                BigDecimal changedAmountToSwap = new BigDecimal(String.valueOf(s));
                if(amountToSwap.compareTo(changedAmountToSwap) == 0) return;
                amountToSwap = changedAmountToSwap;
                int selected_originTokenDecimal = getSelectedOriginalTokenDecimal();
                amountToSwapInSmallUnit = amountToSwap.multiply(BigDecimal.TEN.pow(BigDecimal.valueOf(selected_originTokenDecimal)));
                if(isAllowedToSwap()) {
                    confirmBtn.setEnabled(true);
                    confirmBtn.setText(R.string.swap);
                } else {
                    confirmBtn.setEnabled(false);
                    confirmBtn.setText(R.string.error_insufficient_balance);
                }
                if(amountToSwap.compareTo(BigDecimal.valueOf(0)) == 0 || changedAmountToSwap.compareTo(BigDecimal.valueOf(0)) == 0) {
                    predictValueLabel.setText("0");
                    return;
                }
                fetchPredictVal();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chain_spinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected_chainId = chainData.getChain(position).get("chainId").getAsInt();
                    selected_position = position;
                    listViewForTransferToken.setAdapter(transfer_adapter.get(position));
                    listViewForReceiveToken.setAdapter(receive_adapter.get(position));
                    transfer_adapter.get(position).notifyDataSetChanged();
                    receive_adapter.get(position).notifyDataSetChanged();
                    listViewForTransferToken.performItemClick(
                            listViewForTransferToken.getChildAt(0),
                            0,
                            listViewForTransferToken.getItemIdAtPosition(0)
                    );
                    listViewForReceiveToken.performItemClick(
                            listViewForReceiveToken.getChildAt(1),
                            1,
                            listViewForReceiveToken.getItemIdAtPosition(1)
                    );
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
        });

        ImageView btnOpenSlippageSettingModal = view.findViewById(R.id.swap_setting_btn);

        btnOpenSlippageSettingModal.setOnClickListener(
                v -> {
                    SwapSlippageSettingDialog slippageSettingDialog =
                            new SwapSlippageSettingDialog();
                    slippageSettingDialog.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
                });



        exchangeBtn.setOnClickListener(view1 -> {
            int tmpRecPos = selected_transfer_position, tmpTraPos = selected_receive_position;
            listViewForTransferToken.performItemClick(
                listViewForTransferToken.getChildAt(tmpTraPos),
                    tmpTraPos,
                listViewForTransferToken.getItemIdAtPosition(tmpTraPos)
            );
            listViewForReceiveToken.performItemClick(
                    listViewForTransferToken.getChildAt(tmpRecPos),
                    tmpRecPos,
                    listViewForTransferToken.getItemIdAtPosition(tmpRecPos)
            );
        });

        confirmBtn.setOnClickListener(view1 -> {
            if((String.valueOf(predictValueLabel.getText()).equals("0"))) {
                showToast("Unsuppoted Swap pair!!!");
                return;
            }
            new GetGasPriceTask().execute();
            new TokenSwapTask().execute();
        });

        confirmBtn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#07ad1d")); // pressed color
                        break;
                    case MotionEvent.ACTION_HOVER_ENTER:
                        v.setBackgroundColor(Color.parseColor("#0be328")); // hovered color
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT, MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.parseColor("#42f554")); // default color
                        break;
                }
                return false;
            }
        });

        initTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);

        listViewForTransferToken.performItemClick(
                listViewForTransferToken.getChildAt(selected_transfer_position),
                selected_transfer_position,
                listViewForTransferToken.getItemIdAtPosition(selected_transfer_position)
        );
        listViewForReceiveToken.performItemClick(
                listViewForTransferToken.getChildAt(selected_receive_position),
                selected_receive_position,
                listViewForTransferToken.getItemIdAtPosition(selected_receive_position)
        );


        return view;
    }

    private void updateDataForSwap() {

        amountEdit.setText("0");
        predictValueLabel.setText("0");
        amountToSwap = BigDecimal.valueOf(0);
        timerValue = 16;
        timerLabel.setText(R.string.time_quote);

        resultView.setVisibility(View.GONE);

        fetchAvailableBalance();
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchPredictPirceTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            String swapRouterAddress = chainData.getChain(selected_position).get("swapRouterAddress").getAsString();
            web3 = Web3j.build(new HttpService(chainData.getChain(selected_position).get("rpcURL").getAsString()));
            String selected_originToken_address = getSelectedOriginalTokenAddress();
            String selected_toToken_address = getSelectedToTokenAddress();
            List<String> path = Arrays.asList(selected_originToken_address, selected_toToken_address);
            Function function = new Function("getAmountsOut", Arrays.asList(
                    new Uint256(amountToSwapInSmallUnit.toBigInteger()),
                    new DynamicArray<>(
                            Address.class,
                            path.stream().map(Address::new).collect(Collectors.toList())
                    )
            ), Collections.singletonList(new TypeReference<DynamicArray<Uint256>>() {})
            );
            String encodedFunction = FunctionEncoder.encode(function);
            try {
                EthCall response = web3.ethCall(Transaction.createEthCallTransaction(
                                currentWalletAddress, swapRouterAddress, encodedFunction),
                        DefaultBlockParameterName.LATEST).send();

                List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
                if(decoded.isEmpty()) {
                    return null;
                }
                return ((DynamicArray<Uint256>) decoded.get(0)).getValue().stream().map(Uint256::getValue).collect(Collectors.toList()).toString();
            } catch (IOException e) {
                return null;
            }
        }
        @SuppressLint("ResourceAsColor")
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                String res = result.substring(1, result.length() - 1);
                String[] amounts = res.split(",");
                String amountStr = amounts[1].trim();
                int decimal = getSelectedToTokenDecimal();

                double doubleValue = Double.parseDouble(amountStr);
                double doubleResValue = doubleValue / Math.pow(10, decimal);

                amountToOut = BigDecimal.valueOf(doubleResValue);

                @SuppressLint("DefaultLocale") String formattedValue = String.format("%.6f", doubleResValue);
                predictValueLabel.setText(formattedValue);
                new GetGasPriceTask().execute();
            } else {
                showToast("Unsupported Pair");
            }
        }
    }
    @SuppressLint("DefaultLocale")
    private void showInfos() {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString fromTokenText = new SpannableString("From: "+textviewForTransferToken.getText()+"                    ");
        fromTokenText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, fromTokenText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fromTokenText.setSpan(new StyleSpan(Typeface.NORMAL), 0, fromTokenText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString balaceTotransfer = new SpannableString("-"+amountEdit.getText()+"\n");
        balaceTotransfer.setSpan(new ForegroundColorSpan(Color.RED), 0, balaceTotransfer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        balaceTotransfer.setSpan(new StyleSpan(Typeface.NORMAL), 0, balaceTotransfer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString toTokenText = new SpannableString("To:      "+textviewForReceiveToken.getText()+"                  ");
        toTokenText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, toTokenText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toTokenText.setSpan(new StyleSpan(Typeface.NORMAL), 0, toTokenText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString balaceToreceive = new SpannableString("+"+predictValueLabel.getText()+"\n");
        balaceToreceive.setSpan(new ForegroundColorSpan(Color.BLUE), 0, balaceToreceive.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        balaceToreceive.setSpan(new StyleSpan(Typeface.NORMAL), 0, balaceToreceive.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString gasFeeLabel = new SpannableString("Estimated gas fee:      ");
        gasFeeLabel.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, gasFeeLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gasFeeLabel.setSpan(new StyleSpan(Typeface.NORMAL), 0, gasFeeLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString gasFee;
        String formattedValue;
        if(gasUsdValue == null) {
             gasFee = new SpannableString("Infinite Value"+"\n");
        } else {
             formattedValue = String.format("%.6f", gasUsdValue.doubleValue());
             gasFee = new SpannableString("$"+formattedValue+"\n");
        }
        gasFee.setSpan(new ForegroundColorSpan(Color.BLUE), 0, gasFee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gasFee.setSpan(new StyleSpan(Typeface.NORMAL), 0, gasFee.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(fromTokenText);
        builder.append(balaceTotransfer);
        builder.append(toTokenText);
        builder.append(balaceToreceive);
        builder.append(gasFeeLabel);
        builder.append(gasFee);
        resultView.setText(builder);
        resultView.setVisibility(View.VISIBLE);
    }

    public class TokenSwapTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            isSwaping = true;
            web3 = Web3j.build(new HttpService(chainData.getChain(selected_position).get("rpcURL").getAsString()));
            Credentials credentials  = Credentials.create(privateKey);

            swapContractOnBSC = SwapContractOnBSC.load(
                    chainData.getChain(selected_position).get("swapContractAddress").getAsString(),
                    web3,
                    credentials,
                    currentGasPrice.toBigInteger(),
                    new BigInteger("300000")
            );
            String fromTokenAddress = getSelectedOriginalTokenAddress();
            String toTokenAddress = getSelectedToTokenAddress();
            TransactionReceipt transactionResponse = null;
            if(isNativeAddress(fromTokenAddress)) {
                try {
                    Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzzzzz  native " +currentGasPrice +"    "+ fromTokenAddress + "\n" + toTokenAddress + "\n" + amountToSwapInSmallUnit.toBigInteger() + "\n" + privateKey + "\n");
                    transactionResponse = swapContractOnBSC.swapETH(fromTokenAddress, amountToSwapInSmallUnit.toBigInteger(), toTokenAddress,amountToSwapInSmallUnit.toBigInteger()).send();
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    //showToast(e.getMessage());
                    Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzz  error    " + e.getMessage() + "\n");
                    return null;
                }
            } else {
                try {
                    Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzzzzz  native " +currentGasPrice +"    "+ fromTokenAddress + "\n" + toTokenAddress + "\n" + amountToSwapInSmallUnit.toBigInteger() + "\n" + privateKey + "\n");
                    transactionResponse = swapContractOnBSC.swapETH(fromTokenAddress, amountToSwapInSmallUnit.toBigInteger(), toTokenAddress, BigInteger.ZERO).send();
                } catch (Exception e) {
                    //showToast(e.getMessage());
                    Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzz  error    " + e.getMessage() + "\n");
                    return null;
                }
            }

            if(transactionResponse != null) {
                return transactionResponse.getTransactionHash();
            } else {
                return null;
            }
//            if(selected_chainId == 1) {
//
//
//            } else {
//                web3 = Web3j.build(new HttpService(RPC_URL_ETH));
//                Credentials credentials  = Credentials.create(privateKey);
//
//                swapContractOnETH = SwapContractOnETH.load(swapContractAddressOnETH, web3, credentials, currentGasPrice.toBigInteger(), new BigInteger("300000"));
//                String fromTokenAddress = getSelectedOriginalTokenAddress();
//                String toTokenAddress = getSelectedToTokenAddress();
//                TransactionReceipt transactionResponse = null;
//                if(isNativeAddress(fromTokenAddress)) {
//                    try {
//                        Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzzzzz  native " +currentGasPrice +"    "+ fromTokenAddress + "\n" + toTokenAddress + "\n" + amountToSwapInSmallUnit.toBigInteger() + "\n" + privateKey + "\n");
//                        transactionResponse = swapContractOnETH.swapETH(fromTokenAddress, amountToSwapInSmallUnit.toBigInteger(), toTokenAddress,amountToSwapInSmallUnit.toBigInteger()).send();
//                    } catch (Exception e) {
//                        //throw new RuntimeException(e);
//                        //showToast(e.getMessage());
//                        Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzz  error    " + e.getMessage() + "\n");
//                        return null;
//                    }
//                } else {
//                    try {
//                        Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzzzzz  native " +currentGasPrice +"    "+ fromTokenAddress + "\n" + toTokenAddress + "\n" + amountToSwapInSmallUnit.toBigInteger() + "\n" + privateKey + "\n");
//                        transactionResponse = swapContractOnETH.swapETH(fromTokenAddress, amountToSwapInSmallUnit.toBigInteger(), toTokenAddress, BigInteger.ZERO).send();
//                    } catch (Exception e) {
//                        //throw new RuntimeException(e);
//                        //showToast(e.getMessage());
//                        Timber.e("zzzzzzzzzzzzzzzzzzzzzzzzz  error    " + e.getMessage() + "\n");
//                        return null;
//                    }
//                }
//                if(transactionResponse != null) {
//                    return transactionResponse.getTransactionHash();
//                } else {
//                    return null;
//                }
//            }
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            isSwaping = false;
            if (result != null) {
                resultView.setText("From: "+"selected_originToken_address"+"\n"+"To: "+"selected_toToken_address"+"\n"+"Estimated Gas Price: "+currentGasPrice+"\n"+"Transaction Hash: "+result);
                showToast("Your transaction is pending. It takes couple of minutes...." + result);
            } else {
                showToast("Transaction Failed!!!" + "\n" + "Please check balance and gas fee and try to decrease amont to swap.");
                //resultView.setText("Your balance is not enough for amount of swap including gas fee....");
            }
        }
    }
    private class GetGasPriceTask extends AsyncTask<Void, Void, BigDecimal> {
        @Override
        protected BigDecimal doInBackground(Void... voids) {
            web3 = Web3j.build(new HttpService(chainData.getChain(selected_position).get("rpcURL").getAsString()));
            try {
                EthGasPrice ethGasPrice = web3.ethGasPrice().send();
                return new BigDecimal(ethGasPrice.getGasPrice());
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(BigDecimal gasPrice) {
            if (gasPrice != null) {
                currentGasPrice = gasPrice;
            }
            try {
                fetchWEIToUsdRate(currentGasPrice);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            isFetching = false;
        }
    }

    private boolean isValidAddress(String input) {
        return input.matches("^0x[a-fA-F0-9]{40}$");
    }

    private void fetchWEIToUsdRate(BigDecimal gasPriceInGwei) throws IOException, JSONException {

        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getJSONObject("ethereum").get("usd").toString();
                            double rate = Double.parseDouble(res);
                            if(currentGasPrice != null) {
                                BigDecimal gasPriceInEth = (new BigDecimal("200000").multiply(gasPriceInGwei)).divide(BigDecimal.TEN.pow(BigDecimal.valueOf(18)), 18, BigDecimal.ROUND_DOWN);

                                gasUsdValue = gasPriceInEth.multiply(new BigDecimal(rate));
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("SetTextI18n")
    public void fetchAvailableBalance() {

        toTokenBalance = BigDecimal.valueOf(0);
        fromTokenBalance = BigDecimal.valueOf(0);

        fromBalanceLabel.setText("0 available");
        toBalanceLabel.setText("0 available");

        String url;
        url = "https://www.oklink.com/api/v5/explorer/address/token-balance?chainShortName="+chainData.getChain(selected_position).get("shortName").getAsString()+"&protocolType=token_20&address=" + currentWalletAddress;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String selected_originToken_address = getSelectedOriginalTokenAddress();
                            String selected_toToken_address = getSelectedToTokenAddress();

                            int selected_originTokenDecimal = getSelectedOriginalTokenDecimal();
                            int selected_toTokenDecimal = getSelectedToTokenDecimal();

                            new FetchNativeBalanceTask().execute();

                            JSONArray tokenInfos = response.getJSONArray("data").getJSONObject(0).getJSONArray("tokenList");
                            for (int i = 0; i < tokenInfos.length(); i++) {
                                JSONObject tokenInfo = tokenInfos.getJSONObject(i);
                                String tokenAddress = tokenInfo.get("tokenContractAddress").toString();

                                if (selected_originToken_address.equalsIgnoreCase(tokenAddress)) {
                                    String str = tokenInfo.get("holdingAmount").toString();
                                    BigDecimal value = new BigDecimal(str);
                                    double res = value.doubleValue();
                                    @SuppressLint("DefaultLocale") String formattedValue = String.format("%.3f", res);
                                    fromBalanceLabel.setText(formattedValue+"  available");
                                    fromTokenBalance = new BigDecimal(tokenInfo.get("holdingAmount").toString()).multiply(BigDecimal.TEN.pow(BigDecimal.valueOf(selected_originTokenDecimal)));
                                }
                                if (selected_toToken_address.equalsIgnoreCase(tokenAddress)) {
                                    String str = tokenInfo.get("holdingAmount").toString();
                                    BigDecimal value = new BigDecimal(str);
                                    double res = value.doubleValue();
                                    @SuppressLint("DefaultLocale") String formattedValue = String.format("%.3f", res);
                                    toBalanceLabel.setText(formattedValue+"  available");
                                    toTokenBalance = new BigDecimal(tokenInfo.get("holdingAmount").toString()).multiply(BigDecimal.TEN.pow(BigDecimal.valueOf(selected_toTokenDecimal)));
                                }
                            }
                        } catch (JSONException e) {
                            Timber.e(e.toString());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Failed to fetch token info", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("OK-ACCESS-KEY", "7f7150e9-f99e-4e72-bcb2-a436da798000");
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

            requestQueue.add(jsonObjectRequest);
    }

    private void calculatePercentAmount(String percent) {
        String[] tmp = fromBalanceLabel.getText().toString().split(" ");
        BigDecimal maxAvailableAmount;
        if(percent.equals("max")) {
            maxAvailableAmount = new BigDecimal(tmp[0]).multiply(new BigDecimal("0.95"));
        } else  {
            maxAvailableAmount = new BigDecimal(tmp[0]).multiply(new BigDecimal(percent));
        }

        amountEdit.setText(String.valueOf(maxAvailableAmount));
    }

    private void fetchTokenInfo(String contractAddress, Boolean isReceive) {
        String url;
        url = "https://www.oklink.com/api/v5/explorer/token/token-list?chainShortName"+chainData.getChain(selected_position).get("shortName").getAsString()+"&tokenContractAddress=" + contractAddress;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONObject tokenInfo = response.getJSONArray("data").getJSONObject(0).getJSONArray("tokenList").getJSONObject(0);
                        String tokenContractAddress = tokenInfo.get("tokenContractAddress").toString();
                        int decimal = Integer.parseInt(tokenInfo.get("precision").toString());
                        String name = tokenInfo.get("tokenFullName").toString();
                        String symbol = tokenInfo.get("token").toString();
                        String imageUrl = tokenInfo.get("logoUrl").toString();
                        if(isReceive) {
                            DropdownItem newItem = new DropdownItem(true, R.drawable.ic_token_default_eth,   symbol , imageUrl, tokenContractAddress, decimal);
                            receive_adapter.get(selected_position).addItem(newItem);
                            receive_adapter.get(selected_position).notifyDataSetChanged();
                        } else {
                            DropdownItem newItem = new DropdownItem(true, R.drawable.ic_token_default_bnb,  symbol, imageUrl, tokenContractAddress, decimal);
                            transfer_adapter.get(selected_position).addItem(newItem);
                            transfer_adapter.get(selected_position).notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), "Doesn't exist.", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(requireContext(), "Doesn't exist.", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("OK-ACCESS-KEY", "7f7150e9-f99e-4e72-bcb2-a436da798000");
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    //utils functions
    public boolean isNativeAddress(String tokenAddress) {
        JsonArray tokens = chainData.getChain(selected_position).get("tokens").getAsJsonArray();
        return tokenAddress.equals(
            tokens.get(0).getAsJsonObject().get("address").getAsString()
        );
    }
    public boolean isDecimal(String str) {
        String decimalPattern = "^?(\\d+\\.?|\\.\\d+)\\d*$";

        Pattern pattern = Pattern.compile(decimalPattern);
        return pattern.matcher(str).matches();
    }
    public boolean isAllowedToSwap() {
        new GetGasPriceTask().execute();
        Timber.e("zzzzzzzzzzzzzzzzzzzzzzzz " + currentGasPrice);
        BigDecimal tmpGasPrice = (selected_chainId == 0) ? BigDecimal.valueOf(3e9) : BigDecimal.valueOf(1e9);
        //BigDecimal tmpGasFee = currentGasPrice.multiply(BigDecimal.valueOf(200000));
        //return fromTokenBalance.compareTo(amountToSwapInSmallUnit.add(tmpGasFee)) > 0;
        return fromTokenBalance.compareTo(amountToSwapInSmallUnit) > 0;
    }
    public String getSelectedOriginalTokenAddress() {
        return  Objects.requireNonNull(transfer_adapter.get(selected_position).getItem(selected_transfer_position)).getAddress();
    }
    public String getSelectedToTokenAddress() {
        return Objects.requireNonNull(receive_adapter.get(selected_position).getItem(selected_receive_position)).getAddress();
    }
    public int getSelectedOriginalTokenDecimal() {
        return Objects.requireNonNull(transfer_adapter.get(selected_position).getItem(selected_transfer_position)).getDecimal();
    }
    public int getSelectedToTokenDecimal() {
        return Objects.requireNonNull(transfer_adapter.get(selected_position).getItem(selected_receive_position)).getDecimal();
    }

    private BigDecimal getNativeBalance() throws Exception {
        web3 = Web3j.build(new HttpService(chainData.getChain(selected_position).get("rpcURL").getAsString()));
        EthGetBalance ethGetBalance = web3.ethGetBalance(currentWalletAddress, DefaultBlockParameterName.LATEST).send();
        BigDecimal wei = new BigDecimal(ethGetBalance.getBalance());
        return wei;
    }

    public class FetchNativeBalanceTask extends AsyncTask<Void, Void, String> {
        @SuppressLint("SetTextI18n")
        @Override
        protected String doInBackground(Void... voids) {
            BigDecimal result = BigDecimal.valueOf(0);
            try {
                result = getNativeBalance();
            } catch (Exception e) {
                Timber.e(e.toString());
            }
            return result.toString();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            BigDecimal ethBalanceInWei = new BigDecimal(result);
            String selected_originToken_address = getSelectedOriginalTokenAddress();
            String selected_toToken_address = getSelectedToTokenAddress();

            BigDecimal ethBalanceInEth = ethBalanceInWei.divide(BigDecimal.TEN.pow(BigDecimal.valueOf(18)), 6, BigDecimal.ROUND_DOWN);

            if(isNativeAddress(selected_originToken_address)) {
                fromTokenBalance = ethBalanceInWei;
                fromBalanceLabel.setText(ethBalanceInEth.toString() + " available");
            } else if(isNativeAddress(selected_toToken_address)) {
                toTokenBalance = ethBalanceInWei;
                toBalanceLabel.setText(ethBalanceInEth.toString() + " available");
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void fetchPredictVal() {
        isFetching = true;
        timerValue = 16;
        timerLabel.setText(R.string.time_quote);
        BigDecimal onePercent = amountToSwapInSmallUnit.multiply(new BigDecimal("0.01"));
        amountToSwapInSmallUnit = amountToSwapInSmallUnit.subtract(onePercent);
        new FetchPredictPirceTask().execute();
    }
    public void initTimerTask() {
        timerTask = new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if(isSwaping) {
                    mHandler.post(() -> {
                        spinner.setVisibility(View.VISIBLE);
                        resultView.setVisibility(View.GONE);
                    });
                } else {
                    if(timerValue == 0) {
                        timerValue = 16;
                        mHandler.post(() -> {
                            fetchPredictVal();
                        });
                    } else {
                        if(!amountToSwap.equals(BigDecimal.valueOf(0))) {
                            if(isFetching) {
                                mHandler.post(() -> {
                                    spinner.setVisibility(View.VISIBLE);
                                    resultView.setVisibility(View.GONE);
                                });
                            }
                            else {
                                mHandler.post(() -> {
                                    spinner.setVisibility(View.GONE);
                                });
                            }

                            if(!isFetching) {
                                mHandler.post(() -> {
                                    if(timerValue < 10) {
                                        timerLabel.setText("00:0"+ timerValue);
                                    } else {
                                        timerLabel.setText("00:"+ timerValue);
                                    }
                                });
                                timerValue --;

                                mHandler.post(() -> {
                                    resultView.setVisibility(View.VISIBLE);
                                    showInfos();
                                });
                            }
                        }
                    }
                }
            }
        };
    }
}

