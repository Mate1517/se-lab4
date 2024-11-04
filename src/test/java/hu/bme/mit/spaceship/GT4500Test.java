package hu.bme.mit.spaceship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class GT4500Test {

    private GT4500 spaceship;
    private TorpedoStore mockPrimaryStore;
    private TorpedoStore mockSecondaryStore;

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        spaceship = new GT4500();

        // Mockoljuk a torpedó tárakat
        mockPrimaryStore = mock(TorpedoStore.class);
        mockSecondaryStore = mock(TorpedoStore.class);

        // Reflection használata a privát mezők beállításához
        Field primaryStoreField = spaceship.getClass().getDeclaredField("primaryTorpedoStore");
        Field secondaryStoreField = spaceship.getClass().getDeclaredField("secondaryTorpedoStore");

        primaryStoreField.setAccessible(true);
        secondaryStoreField.setAccessible(true);

        primaryStoreField.set(spaceship, mockPrimaryStore);
        secondaryStoreField.set(spaceship, mockSecondaryStore);
    }

    @Test
    public void fireTorpedo_SingleMode_PrimaryStoreFired() {
        // Arrange
        when(mockPrimaryStore.fire(1)).thenReturn(true);

        // Act
        boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

        // Assert
        assertTrue(result);
        verify(mockPrimaryStore, times(1)).fire(1);
        verify(mockSecondaryStore, never()).fire(anyInt());
    }

    @Test
    public void fireTorpedo_SingleMode_AlternatingFiring() {
        when(mockPrimaryStore.fire(1)).thenReturn(true);
        when(mockSecondaryStore.fire(1)).thenReturn(true);

        spaceship.fireTorpedo(FiringMode.SINGLE);
        spaceship.fireTorpedo(FiringMode.SINGLE);

        // Ellenőrizzük, hogy mindkét torpedó tár tüzelésre került-e
        verify(mockPrimaryStore, times(1)).fire(1);
        verify(mockSecondaryStore, times(1)).fire(1);
    }

    @Test
    public void fireTorpedo_SingleMode_PrimaryStoreFailure() {
        when(mockPrimaryStore.fire(1)).thenReturn(false);

        boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

        // Ellenőrizzük, hogy sikertelen a tüzelés, ha az elsődleges tár meghibásodik
        assertFalse(result);
        verify(mockPrimaryStore, times(1)).fire(1);
        verify(mockSecondaryStore, times(0)).fire(1);
    }

    @Test
public void fireTorpedo_SingleMode_PrimaryStoreEmpty() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(true);
    when(mockSecondaryStore.isEmpty()).thenReturn(false);
    when(mockSecondaryStore.fire(1)).thenReturn(true);

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(mockPrimaryStore, never()).fire(anyInt());
    verify(mockSecondaryStore, times(1)).fire(1);
}

@Test
public void fireTorpedo_SingleMode_BothStoresEmpty() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(true);
    when(mockSecondaryStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
    verify(mockPrimaryStore, never()).fire(anyInt());
    verify(mockSecondaryStore, never()).fire(anyInt());
}

@Test
public void fireTorpedo_SingleMode_PrimaryStoreFires() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(false); // primary store nem üres
    when(mockPrimaryStore.fire(1)).thenReturn(true); // a tűz sikeres
    when(mockSecondaryStore.isEmpty()).thenReturn(true); // secondary store üres

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(mockPrimaryStore, times(1)).fire(1); // Ellenőrizzük, hogy a primary store tűzelt
    verify(mockSecondaryStore, never()).fire(anyInt()); // secondary store nem tüzelhetett
}

@Test
public void fireTorpedo_SingleMode_SecondaryStoreFires() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(true); // primary store üres
    when(mockSecondaryStore.isEmpty()).thenReturn(false); // secondary store nem üres
    when(mockSecondaryStore.fire(1)).thenReturn(true); // a tűz sikeres

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
    verify(mockSecondaryStore, times(1)).fire(1); // Ellenőrizzük, hogy a secondary store tűzelt
    verify(mockPrimaryStore, never()).fire(anyInt()); // primary store nem tüzelhetett
}

@Test
public void fireTorpedo_AllMode_BothStoresFired() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(false); // primary store nem üres
    when(mockSecondaryStore.isEmpty()).thenReturn(false); // secondary store sem üres
    when(mockPrimaryStore.fire(1)).thenReturn(true); // primary store tűzelt
    when(mockSecondaryStore.fire(1)).thenReturn(true); // secondary store tűzelt

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertTrue(result);
    verify(mockPrimaryStore, times(1)).fire(1); // Ellenőrizzük, hogy a primary store tűzelt
    verify(mockSecondaryStore, times(1)).fire(1); // Ellenőrizzük, hogy a secondary store is tűzelt
}

@Test
public void fireTorpedo_AllMode_OneStoreEmpty() {
    // Arrange
    when(mockPrimaryStore.isEmpty()).thenReturn(true); // primary store üres
    when(mockSecondaryStore.isEmpty()).thenReturn(false); // secondary store nem üres
    when(mockSecondaryStore.fire(1)).thenReturn(true); // secondary store tűzelt

    // Act
    boolean result = spaceship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertTrue(result);
    verify(mockPrimaryStore, never()).fire(anyInt()); // primary store nem tüzelhetett
    verify(mockSecondaryStore, times(1)).fire(1); // secondary store tűzelt
}


}
