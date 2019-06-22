package net.twerion.hungergames.locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

// This is not a unit test. It reads a resource bundle from disk.
public class ResourceBundleMessageStoreTest {
  private static final String GREETING_KEY = "test.greeting";
  private static final String MISSING_KEY = "this.key.is.missing";

  private ResourceBundle bundle;

  @Before
  public void initializeTest() {
    this.bundle = loadBundle();
  }

  private ResourceBundle loadBundle() {
    return ResourceBundle.getBundle("test", Locale.ENGLISH);
  }

  @Test
  public void testFallbackMessage() {
    String fallbackMessage = "fallback";
    MessageStore store = ResourceBundleMessageStore.create(bundle, fallbackMessage, "");

    String message = store.find(MISSING_KEY);
    Assert.assertEquals(fallbackMessage, message);
  }

  @Test
  public void testFallbackMessageIsNotFormatted() {
    String fallbackMessage = "%s";
    MessageStore store = ResourceBundleMessageStore.create(bundle, fallbackMessage, "");
    String message = store.find(MISSING_KEY, "Hello");
    Assert.assertEquals(message, fallbackMessage);
  }
}
