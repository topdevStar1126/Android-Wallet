package com.alphawallet.app.swapContractOnEth;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class SwapContractOnETH extends Contract {
    private static final String BINARY = "0x60806040526004361061007f5760003560e01c8063ae91cfdd1161004e578063ae91cfdd14610121578063c22b8ec61461013d578063f2fde38b14610169578063f887ea401461019257610086565b80631587c1e81461008b578063715018a6146100c85780638da5cb5b146100df57806399d32fc41461010a57610086565b3661008657005b600080fd5b34801561009757600080fd5b506100b260048036038101906100ad9190611288565b6101bd565b6040516100bf91906112f6565b60405180910390f35b3480156100d457600080fd5b506100dd6101fc565b005b3480156100eb57600080fd5b506100f4610210565b60405161010191906112f6565b60405180910390f35b34801561011657600080fd5b5061011f610239565b005b61013b6004803603810190610136919061133d565b6106a2565b005b34801561014957600080fd5b50610152610cea565b60405161016092919061150c565b60405180910390f35b34801561017557600080fd5b50610190600480360381019061018b9190611543565b610ecd565b005b34801561019e57600080fd5b506101a7610f53565b6040516101b491906115cf565b60405180910390f35b600281815481106101cd57600080fd5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b610204610f79565b61020e6000611000565b565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b610241610f79565b60005b600280549050811015610658576000600267ffffffffffffffff81111561026e5761026d6115ea565b5b60405190808252806020026020018201604052801561029c5781602001602082028036833780820191505090505b509050600282815481106102b3576102b2611619565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16816000815181106102f2576102f1611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ad5c46486040518163ffffffff1660e01b8152600401602060405180830381865afa158015610399573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906103bd919061165d565b816001815181106103d1576103d0611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250506002828154811061041f5761041e611619565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663095ea7b373f164fc0ec4e93095b804a4795bbe1e041497b92a600160008560008151811061049257610491611619565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518363ffffffff1660e01b81526004016104f2929190611699565b6020604051808303816000875af1158015610511573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061053591906116fa565b50600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166318cbafe5600160008460008151811061058c5761058b611619565b5b602002602001015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205460008430610384426105e19190611756565b6040518663ffffffff1660e01b81526004016106019594939291906117c5565b6000604051808303816000875af1158015610620573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250810190610649919061195e565b50508080600101915050610244565b3373ffffffffffffffffffffffffffffffffffffffff166108fc479081150290604051600060405180830381858888f1935050505015801561069e573d6000803e3d6000fd5b5050565b600083905060003403610a96578073ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff1660e01b81526004016106ec939291906119a7565b6020604051808303816000875af115801561070b573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061072f91906116fa565b506000600267ffffffffffffffff81111561074d5761074c6115ea565b5b60405190808252806020026020018201604052801561077b5781602001602082028036833780820191505090505b509050848160008151811061079357610792611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff168152505082816001815181106107e2576107e1611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050600061082886866110c4565b90506000818661083891906119de565b90508373ffffffffffffffffffffffffffffffffffffffff1663095ea7b373f164fc0ec4e93095b804a4795bbe1e041497b92a836040518363ffffffff1660e01b8152600401610889929190611699565b6020604051808303816000875af11580156108a8573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906108cc91906116fa565b5073c02aaa39b223fe8d0a0e5c4f27ead9083c756cc273ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff16036109d357600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166318cbafe58260008633610384426109659190611756565b6040518663ffffffff1660e01b81526004016109859594939291906117c5565b6000604051808303816000875af11580156109a4573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052508101906109cd919061195e565b50610a8e565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338ed1739826000863361038442610a249190611756565b6040518663ffffffff1660e01b8152600401610a449594939291906117c5565b6000604051808303816000875af1158015610a63573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250810190610a8c919061195e565b505b505050610ce4565b6000600267ffffffffffffffff811115610ab357610ab26115ea565b5b604051908082528060200260200182016040528015610ae15781602001602082028036833780820191505090505b509050600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ad5c46486040518163ffffffff1660e01b8152600401602060405180830381865afa158015610b51573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610b75919061165d565b81600081518110610b8957610b88611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508281600181518110610bd857610bd7611619565b5b602002602001019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637ff36ab5606434610c5d9190611a41565b34610c6891906119de565b6000843361038442610c7a9190611756565b6040518663ffffffff1660e01b8152600401610c999493929190611a72565b60006040518083038185885af1158015610cb7573d6000803e3d6000fd5b50505050506040513d6000823e3d601f19601f82011682018060405250810190610ce1919061195e565b50505b50505050565b60608060006001600280549050610d019190611756565b67ffffffffffffffff811115610d1a57610d196115ea565b5b604051908082528060200260200182016040528015610d485781602001602082028036833780820191505090505b50905060005b600280549050811015610e05576001600060028381548110610d7357610d72611619565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054828281518110610dec57610deb611619565b5b6020026020010181815250508080600101915050610d4e565b670de0b6b3a764000047610e199190611abe565b828281518110610e2c57610e2b611619565b5b60200260200101818152505060028281805480602002602001604051908101604052809291908181526020018280548015610ebc57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610e72575b505050505091509350935050509091565b610ed5610f79565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610f475760006040517f1e4fbdf7000000000000000000000000000000000000000000000000000000008152600401610f3e91906112f6565b60405180910390fd5b610f5081611000565b50565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b610f81611236565b73ffffffffffffffffffffffffffffffffffffffff16610f9f610210565b73ffffffffffffffffffffffffffffffffffffffff1614610ffe57610fc2611236565b6040517f118cdaa7000000000000000000000000000000000000000000000000000000008152600401610ff591906112f6565b60405180910390fd5b565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b6000806064836110d49190611a41565b905060005b600280549050811015611165578473ffffffffffffffffffffffffffffffffffffffff166002828154811061111157611110611619565b5b9060005260206000200160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1603156111655780806001019150506110d9565b60028054905081036111d5576002859080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b81600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282546112249190611756565b92505081905550819250505092915050565b600033905090565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b61126581611252565b811461127057600080fd5b50565b6000813590506112828161125c565b92915050565b60006020828403121561129e5761129d611248565b5b60006112ac84828501611273565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006112e0826112b5565b9050919050565b6112f0816112d5565b82525050565b600060208201905061130b60008301846112e7565b92915050565b61131a816112d5565b811461132557600080fd5b50565b60008135905061133781611311565b92915050565b60008060006060848603121561135657611355611248565b5b600061136486828701611328565b935050602061137586828701611273565b925050604061138686828701611328565b9150509250925092565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6113c5816112d5565b82525050565b60006113d783836113bc565b60208301905092915050565b6000602082019050919050565b60006113fb82611390565b611405818561139b565b9350611410836113ac565b8060005b8381101561144157815161142888826113cb565b9750611433836113e3565b925050600181019050611414565b5085935050505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b61148381611252565b82525050565b6000611495838361147a565b60208301905092915050565b6000602082019050919050565b60006114b98261144e565b6114c38185611459565b93506114ce8361146a565b8060005b838110156114ff5781516114e68882611489565b97506114f1836114a1565b9250506001810190506114d2565b5085935050505092915050565b6000604082019050818103600083015261152681856113f0565b9050818103602083015261153a81846114ae565b90509392505050565b60006020828403121561155957611558611248565b5b600061156784828501611328565b91505092915050565b6000819050919050565b600061159561159061158b846112b5565b611570565b6112b5565b9050919050565b60006115a78261157a565b9050919050565b60006115b98261159c565b9050919050565b6115c9816115ae565b82525050565b60006020820190506115e460008301846115c0565b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60008151905061165781611311565b92915050565b60006020828403121561167357611672611248565b5b600061168184828501611648565b91505092915050565b61169381611252565b82525050565b60006040820190506116ae60008301856112e7565b6116bb602083018461168a565b9392505050565b60008115159050919050565b6116d7816116c2565b81146116e257600080fd5b50565b6000815190506116f4816116ce565b92915050565b6000602082840312156117105761170f611248565b5b600061171e848285016116e5565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600061176182611252565b915061176c83611252565b925082820190508082111561178457611783611727565b5b92915050565b6000819050919050565b60006117af6117aa6117a58461178a565b611570565b611252565b9050919050565b6117bf81611794565b82525050565b600060a0820190506117da600083018861168a565b6117e760208301876117b6565b81810360408301526117f981866113f0565b905061180860608301856112e7565b611815608083018461168a565b9695505050505050565b600080fd5b6000601f19601f8301169050919050565b61183e82611824565b810181811067ffffffffffffffff8211171561185d5761185c6115ea565b5b80604052505050565b600061187061123e565b905061187c8282611835565b919050565b600067ffffffffffffffff82111561189c5761189b6115ea565b5b602082029050602081019050919050565b600080fd5b6000815190506118c18161125c565b92915050565b60006118da6118d584611881565b611866565b905080838252602082019050602084028301858111156118fd576118fc6118ad565b5b835b81811015611926578061191288826118b2565b8452602084019350506020810190506118ff565b5050509392505050565b600082601f8301126119455761194461181f565b5b81516119558482602086016118c7565b91505092915050565b60006020828403121561197457611973611248565b5b600082015167ffffffffffffffff8111156119925761199161124d565b5b61199e84828501611930565b91505092915050565b60006060820190506119bc60008301866112e7565b6119c960208301856112e7565b6119d6604083018461168a565b949350505050565b60006119e982611252565b91506119f483611252565b9250828203905081811115611a0c57611a0b611727565b5b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601260045260246000fd5b6000611a4c82611252565b9150611a5783611252565b925082611a6757611a66611a12565b5b828204905092915050565b6000608082019050611a8760008301876117b6565b8181036020830152611a9981866113f0565b9050611aa860408301856112e7565b611ab5606083018461168a565b95945050505050565b6000611ac982611252565b9150611ad483611252565b9250828202611ae281611252565b91508282048414831517611af957611af8611727565b5b509291505056fea2646970667358221220f91dcb1c56f0299e91be0b81a96e0d4b2f38f8e122de1c128ac80a0b4adefd9c64736f6c63430008180033";

    public static final String FUNC_CLAIMFEE = "claimFee";

    public static final String FUNC_FEETOKENLIST = "feeTokenList";

    public static final String FUNC_GETFEETOKENLISTANDAMOUNT = "getFeeTokenListAndAmount";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_ROUTER = "router";

    public static final String FUNC_SWAPETH = "swapETH";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected SwapContractOnETH(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SwapContractOnETH(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SwapContractOnETH(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SwapContractOnETH(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> claimFee() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLAIMFEE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> feeTokenList(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FEETOKENLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> getFeeTokenListAndAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETFEETOKENLISTANDAMOUNT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> router() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ROUTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapETH(String _fromTokenAddress, BigInteger _fromTokenAmount, String _toTokenAddress, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SWAPETH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_fromTokenAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(_fromTokenAmount), 
                new org.web3j.abi.datatypes.Address(_toTokenAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, value);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SwapContractOnETH load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SwapContractOnETH(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SwapContractOnETH load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SwapContractOnETH(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SwapContractOnETH load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SwapContractOnETH(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SwapContractOnETH load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SwapContractOnETH(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SwapContractOnETH> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SwapContractOnETH.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SwapContractOnETH> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SwapContractOnETH.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SwapContractOnETH> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SwapContractOnETH.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SwapContractOnETH> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SwapContractOnETH.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }
}
