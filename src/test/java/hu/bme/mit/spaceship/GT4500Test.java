package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
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
    public void fireTorpedo_AllMode_BothStoresFired() {
        when(mockPrimaryStore.fire(1)).thenReturn(true);
        when(mockSecondaryStore.fire(1)).thenReturn(true);

        boolean result = spaceship.fireTorpedo(FiringMode.ALL);

        // Ellenőrizzük, hogy mindkét torpedó tár tüzelésre került-e
        assertTrue(result);
        verify(mockPrimaryStore, times(1)).fire(1);
        verify(mockSecondaryStore, times(1)).fire(1);
    }

    @Test
    public void fireTorpedo_AllMode_OneStoreEmpty() {
        when(mockPrimaryStore.isEmpty()).thenReturn(true);
        when(mockSecondaryStore.fire(1)).thenReturn(true);

        boolean result = spaceship.fireTorpedo(FiringMode.ALL);

        // Ellenőrizzük, hogy az egyik tár üres volt, mégis sikeres volt a tüzelés
        assertTrue(result);
        verify(mockPrimaryStore, times(0)).fire(1);
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

}
