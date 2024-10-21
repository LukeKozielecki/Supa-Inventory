package luke.koz.supainventory.inventory.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import luke.koz.supainventory.inventory.data.InventoryRepository
import luke.koz.supainventory.inventory.model.GetItemEntry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InventoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: InventoryViewModel

    @Mock
    private lateinit var inventoryRepository: InventoryRepository

    init {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test initial state is Loading`() = runBlocking {
        // Arrange
        viewModel = InventoryViewModel(inventoryRepository)

        // Assert
        assertEquals(ItemListState.Loading, viewModel.itemsListUiState.first())
    }

    @Test
    fun `test successful fetch of items`() = runBlocking {

        // Arrange
        val items = listOf(
            GetItemEntry(id = 1, itemName = "Mock Item 1", itemPrice = 10.0f, itemQuantity = 5),
            GetItemEntry(id = 2, itemName = "Mock Item 2", itemPrice = 15.5f, itemQuantity = 3)
        )
        Mockito.`when`(inventoryRepository.fetchItems()).thenReturn(items)

        viewModel = InventoryViewModel(inventoryRepository)

        // Act
        viewModel.setItemListUiState()

        // Assert
        assertEquals(ItemListState.Success(animals = items), viewModel.itemsListUiState.first())
        assertEquals(items, viewModel.items)
    }

    @Test
    fun `test fetch of items returns empty list`() = runBlocking {
        // Arrange
        val items = emptyList<GetItemEntry>()
        Mockito.`when`(inventoryRepository.fetchItems()).thenReturn(items)

        viewModel = InventoryViewModel(inventoryRepository)

        // Act
        viewModel.setItemListUiState()

        // Assert
        assertEquals(ItemListState.Error("No items found"), viewModel.itemsListUiState.first())
        assert(viewModel.items.isEmpty())
    }
}