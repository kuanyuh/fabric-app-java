package xyz.kuanyu.fabric;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/asset")
@Slf4j
@AllArgsConstructor
public class AssetContractController {

    final Gateway gateway;

    @GetMapping("/{assetID}")
    public Map<String , Object> queryAssetByID(@PathVariable String assetID) throws ContractException {

        Map<String , Object> result = Maps.newConcurrentMap();
        Contract contract = getContract();
        byte[] asset = contract.evaluateTransaction("readAsset", assetID);

        result.put("payload" , StringUtils.newStringUtf8(asset));
        result.put("status" , "ok");

        return result;
    }

    @PutMapping("/")
    public Map<String , Object> createAsset(@RequestBody Asset asset) throws ContractException, InterruptedException, TimeoutException {

        Map<String , Object> result = Maps.newConcurrentMap();
        Contract contract = getContract();
        byte[] bytes = contract.submitTransaction("createAsset", asset.getId(), asset.getColor(), String.valueOf(asset.getSize()), asset.getOwner(), String.valueOf(asset.getValue()));

        result.put("payload" , StringUtils.newStringUtf8(bytes));
        result.put("status" , "ok");

        return result;
    }

    @PostMapping("/")
    public Map<String , Object> updateAsset(@RequestBody Asset asset) throws ContractException, InterruptedException, TimeoutException {

        Map<String , Object> result = Maps.newConcurrentMap();
        Contract contract = getContract();
        byte[] bytes = contract.submitTransaction("updateAsset", asset.getId(), asset.getColor(), String.valueOf(asset.getSize()), asset.getOwner(), String.valueOf(asset.getValue()));

        result.put("payload" , StringUtils.newStringUtf8(bytes));
        result.put("status" , "ok");

        return result;
    }

    @PostMapping("/transfer")
    public Map<String , Object> transferAsset(@RequestBody Map map) throws ContractException, InterruptedException, TimeoutException {

        Map<String , Object> result = Maps.newConcurrentMap();
        Contract contract = getContract();
        byte[] bytes = contract.submitTransaction("transfer",
                String.valueOf(map.get("asset_id_from")),
                String.valueOf(map.get("asset_id_to")),
                String.valueOf(map.get("value")));

        result.put("payload" , StringUtils.newStringUtf8(bytes));
        result.put("status" , "ok");

        return result;
    }


    @DeleteMapping("/{assetID}")
    public Map<String , Object> deleteAssetByID(@PathVariable String assetID) throws ContractException, InterruptedException, TimeoutException, TimeoutException {

        Map<String , Object> result = Maps.newConcurrentMap();
        Contract contract = getContract();
        byte[] asset = contract.submitTransaction("deleteAsset", assetID);

        result.put("payload" , StringUtils.newStringUtf8(asset));
        result.put("status" , "ok");

        return result;
    }

    private Contract getContract() {
        // 获取通道
        Network network = gateway.getNetwork("mychannel");
        // 获取合约
        return network.getContract("hyperledger-fabric-contract-java-demo");
    }
}
