package com.hotmail.bstern2011.ItemScan;

import java.util.ArrayList;

public class Message
  implements Runnable
{
  private ArrayList<String> messages;

  public Message(ArrayList<String> broke)
  {
    this.messages = broke;
  }

  public void run()
  {
    for (String message : this.messages) {
      System.out.println(message);
    }
  }
}