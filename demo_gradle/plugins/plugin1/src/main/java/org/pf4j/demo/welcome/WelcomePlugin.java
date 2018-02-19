/*
 * Copyright 2012 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pf4j.demo.welcome;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.demo.api.Greeting;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Decebal Suiu
 */
public class WelcomePlugin extends Plugin {
  private static final Logger logger = LoggerFactory.getLogger(WelcomePlugin.class);

  private Thread task = null;
  AtomicBoolean stop = new AtomicBoolean(false);

  public WelcomePlugin(PluginWrapper wrapper) {
    super(wrapper);
  }

  @Override
  public void start() {
    logger.info("WelcomePlugin.start()");
    task = new Thread(() -> {
      while (!stop.get()) {
        logger.info(StringUtils.upperCase("Welcome Plugin!! xxxooo"));
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
      }
    });
    task.start();
  }

  @Override
  public void stop() {
    logger.info("Stopping WelcomePlugin task...");
    if (task != null) {
      stop.set(true);
      try {
        task.join(5000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    logger.info("Stopped WelcomePlugin task...");
  }
  @Extension
  public static class WelcomeGreeting implements Greeting {

    @Override
    public String getGreeting() {
      return "Welcome";
    }

  }

}
