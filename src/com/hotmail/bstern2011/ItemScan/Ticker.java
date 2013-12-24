package com.hotmail.bstern2011.ItemScan;

public class Ticker
  implements Runnable
{
  private ItemScan plugin;

  public Ticker(ItemScan i)
  {
    this.plugin = i;
  }

  public void run()
  {
    if (this.plugin.instances != null)
      for (Scanner scan : this.plugin.instances) {
        int ticks = scan.getTick();
        int delay = scan.getDelay();
        int pulse = scan.getPulse();
        if (ticks <= delay + pulse) {
          if ((ticks >= pulse + delay) || (ticks <= delay)){
            scan.stoneOff();
          }else
            scan.stoneOn();
            scan.setTick(ticks + 1);
          
        }
        else {
          scan.collectNear();
        }
      }
  }
}