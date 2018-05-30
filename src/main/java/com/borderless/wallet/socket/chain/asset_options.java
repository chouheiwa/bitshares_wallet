package com.borderless.wallet.socket.chain;

import com.borderless.wallet.socket.account_object;
import com.borderless.wallet.socket.fc.io.base_encoder;
import com.borderless.wallet.socket.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.borderless.wallet.socket.chain.config.GRAPHENE_MAX_SHARE_SUPPLY;
import static com.borderless.wallet.socket.chain.types.UIA_ASSET_ISSUER_PERMISSION_MASK;

public class asset_options {
    /// The maximum supply of this asset which may exist at any given time. This can be as large as
    /// GRAPHENE_MAX_SHARE_SUPPLY
    public UnsignedLong max_supply = UnsignedLong.valueOf(GRAPHENE_MAX_SHARE_SUPPLY);
    /// When this asset is traded on the markets, this percentage of the total traded will be exacted and paid
    /// to the issuer. This is a fixed point value, representing hundredths of a percent, i.e. a value of 100
    /// in this field means a 1% fee is charged on market trades of this asset.
    public short market_fee_percent = 0;
    /// Market fees calculated as @ref market_fee_percent of the traded volume are capped to this value
    public long max_market_fee = GRAPHENE_MAX_SHARE_SUPPLY;

    /// The flags which the issuer has permission to update. See @ref asset_issuer_permission_flags
    short issuer_permissions = UIA_ASSET_ISSUER_PERMISSION_MASK;
    /// The currently active flags on this permission. See @ref asset_issuer_permission_flags
    public short flags = 0;

    /// When a non-core asset is used to pay a fee, the blockchain must convert that asset to core asset in
    /// order to accept the fee. If this asset's fee pool is funded, the chain will automatically deposite fees
    /// in this asset to its accumulated fees, and withdraw from the fee pool the same amount as converted at
    /// the core exchange rate.
    public price core_exchange_rate;

    /// A set of accounts which maintain whitelists to consult for this asset. If whitelist_authorities
    /// is non-empty, then only accounts in whitelist_authorities are allowed to hold, use, or transfer the asset.
    public List<object_id<account_object>> whitelist_authorities = new ArrayList<>();
    /// A set of accounts which maintain blacklists to consult for this asset. If flags & white_list is set,
    /// an account may only send, receive, trade, etc. in this asset if none of these accounts appears in
    /// its account_object::blacklisting_accounts field. If the account is blacklisted, it may not transact in
    /// this asset even if it is also whitelisted.
    public List<object_id<account_object>> blacklist_authorities = new ArrayList<>();

    /** defines the assets that this asset may be traded against in the market */
    public List<object_id<asset_object>>   whitelist_markets = new ArrayList<>();
    /** defines the assets that this asset may not be traded against in the market, must not overlap whitelist */
    public List<object_id<asset_object>>   blacklist_markets = new ArrayList<>();

    /**
     * data that describes the meaning/purpose of this asset, fee will be charged proportional to
     * size of description.
     */
    public String description = "";
    //extensions_type extensions;
    public Set extensions = new HashSet();

    public void write_to_encoder(base_encoder baseEncoder) {
        raw_type raw_object = new raw_type();

        baseEncoder.write(raw_object.get_byte_array(max_supply));

        baseEncoder.write(raw_object.get_byte_array(market_fee_percent));

        baseEncoder.write(raw_object.get_byte_array(max_market_fee));

        baseEncoder.write(raw_object.get_byte_array(issuer_permissions));

        baseEncoder.write(raw_object.get_byte_array(flags));

        core_exchange_rate.write_to_encoder(baseEncoder);

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(whitelist_authorities.size()));

        for (object_id id : whitelist_authorities) {
            id.write_to_encoder(baseEncoder);
        }

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(blacklist_authorities.size()));

        for (object_id id : blacklist_authorities) {
            id.write_to_encoder(baseEncoder);
        }

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(whitelist_markets.size()));

        for (object_id id : whitelist_markets) {
            id.write_to_encoder(baseEncoder);
        }

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(blacklist_markets.size()));

        for (object_id id : blacklist_markets) {
            id.write_to_encoder(baseEncoder);
        }

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(description.length()));

        baseEncoder.write(description.getBytes());

        raw_object.pack(baseEncoder,UnsignedInteger.fromIntBits(extensions.size()));
    }
    /**
     * set_Issuer_Permissions
     * size of description.
     */
    public void setIssuer_permissions(boolean charge_market_fee,boolean white_list,boolean override_authority,boolean transfer_restricted,boolean disable_force_settle,boolean global_settle,boolean disable_confidential,boolean witness_fed_asset,boolean committee_fed_asset) {
        short final_permissions = 0;

        if (charge_market_fee) final_permissions += 0x01;
        if (white_list) final_permissions += 0x02;
        if (override_authority) final_permissions += 0x04;
        if (transfer_restricted) final_permissions += 0x08;
        if (disable_force_settle) final_permissions += 0x10;
        if (global_settle) final_permissions += 0x20;
        if (disable_confidential) final_permissions += 0x40;
        if (witness_fed_asset) final_permissions += 0x80;
        if (committee_fed_asset) final_permissions += 0x100;

        issuer_permissions = final_permissions;
    }
    /// Perform internal consistency checks.
    /// @throws fc::exception if any check fails
}
