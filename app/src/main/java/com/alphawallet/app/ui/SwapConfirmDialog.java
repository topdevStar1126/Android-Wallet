package com.alphawallet.app.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alphawallet.app.R;
import com.alphawallet.app.widget.InputView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import java.io.IOException;
import java.util.Optional;

public class SwapConfirmDialog extends BottomSheetDialogFragment {
    ImageView loadingGifImage;

    private Web3j web3;
    int flg = 0;
    private String txHash;
    private int chainId;

    boolean isOK = false;

    public SwapConfirmDialog(String tx, int chain) {
        txHash = tx;
        chainId = chain;
        if(chainId == 0) {
            web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/1b5defa8ed65495c943a0dfa74fd832c"));
        } else {
            web3 = Web3j.build(new HttpService("https://bsc-dataseed.binance.org/"));
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.swap_dialog,  container, false);

        loadingGifImage = v.findViewById(R.id.imageView);

        Glide.with(this)
                .asGif()
                .load(R.raw.loading) // Use the raw resource ID of your GIF file
                .into((ImageView) loadingGifImage);

        new CheckTransactionStatusTask().execute();

        if(isOK) {
            showToast("success");
            loadingGifImage.setImageResource(R.drawable.success);
        } else {
            showToast("fail");
            loadingGifImage.setImageResource(R.drawable.failed);
        }

        return v;
    }

    private class CheckTransactionStatusTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Optional<TransactionReceipt> receiptOptional;
            try {
                receiptOptional = waitForTransactionReceipt(web3, txHash);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get transaction receipt", e);
            }

            if (receiptOptional.isPresent()) {
                TransactionReceipt receipt = receiptOptional.get();
                // Check transaction status
                if (receipt.isStatusOK()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                isOK = true;
            } else {
                isOK = false;
            }
        }
    }

    public void showToast(CharSequence msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private Optional<TransactionReceipt> waitForTransactionReceipt(Web3j web3, String transactionHash) throws Exception {
        // Wait for transaction to be mined
        EthGetTransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        if (transactionReceipt.getTransactionReceipt().isPresent()) {
            return Optional.of(transactionReceipt.getTransactionReceipt().get());
        } else {
            return Optional.empty();
        }
    }
}
