package com.salespage.salespageservice.domains;

import java.util.Arrays;
import java.util.List;

public class Constants {
  public final static String AUTH_KEY = "Authorization";
  public static final String TRANSACTION_CHECKPOINT_ID = "transaction_checkpoint_id";
  public static final String PAYMENT_STATISTIC_CHECKPOINT = "payment_checkpoint_id";

  public static final String PAYMENT_BANK_ACCOUNT = "payment_bank_account";
  public static final List<String> COLOR = Arrays.asList(
      "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF",
      "#00FFFF", "#800000", "#008000", "#000080", "#808080",
      "#C0C0C0", "#800080", "#FFA500", "#008080", "#FFC0CB",
      "#800080", "#008080", "#FF4500", "#4B0082", "#2E8B57"
  );;
}
