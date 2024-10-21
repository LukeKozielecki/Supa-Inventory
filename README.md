# Supabase server integration

Supa inventory app is a shared inventory management app, utilizing a single shared instance of its contents. The app is intended to demonstrate using external server to store information, access and modify it localy via the Internet.

## Features breakdown
- Fetch items from a database, using [Supabase](https://supabase.com/)
- Display items in a list format
- View item details
- Edit, delete, and sell one (of the) items from the inventory
- Interface built with Jetpack Compose along guidelines of Material3 design

## Main feature implementation
>Collapsed for reading convenience, please do expand at will.
<details>
<summary>InventoryRepository.kt</summary>
  
``` Kotlin
/**
 * Repository class responsible for interacting with the Supabase database.
 * It provides methods to fetch items from the database.
 */
class InventoryRepository(private val supabase: SupabaseClient) {
    /**
     * Suspend function to fetch items from the database asynchronously.
     * @return A list of GetItemEntry fetched from the database, or an empty list in case of an error.
     */
    suspend fun fetchItems(): List<GetItemEntry> {
        return try {
            val results = supabase.from("items_table").select().decodeList<GetItemEntry>()
            results
        } catch (e: Exception) {
            // In case of failure (e.g. no internet, firebase gets the high-ground) return an empty list
            emptyList()
        }
    }
}
```
</details>
<details>
<summary>InventoryViewModel.kt</summary>
  
``` Kotlin
class InventoryViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {
    /*exposed and _private variables */
    /**
     * Function to set the UI state for the item list.
     *  - Defaults _itemsListUiState.value to .Loading
     *  - Fetches items from the repository
     *  - Update the items list
     */
    fun setItemListUiState() {
        _itemsListUiState.value = ItemListState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val results = inventoryRepository.fetchItems()

            _items.clear()
            _items.addAll(results)

            _itemsListUiState.value = if (results.isNotEmpty()) {
                ItemListState.Success(animals = results)
            } else {
                ItemListState.Error("No items found")
            }
        }
    }

    init {
        setItemListUiState() // Trigger data loading upon ViewModel creation.
    }

    /**
     * Companion object to provide a factory for the ViewModel, for the sake of dependency injection
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val supabase = createSupabaseClient(
                    supabaseUrl = "https://qsdihmtmasiosykepcwm.supabase.co",
                    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFzZGlobXRtYXNpb3N5a2VwY3dtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NjMyMDYsImV4cCI6MjA0MjIzOTIwNn0.ygESpL0irLfa8o3PAvsDTSqtNKVj_sp33bm5k0HlOso"
                ) {
                    install(Postgrest)
                }
                val repository = InventoryRepository(supabase)
                InventoryViewModel(repository)
            }
        }
    }
}
```
</details>
<details>
<summary>InventoryNavHost</summary>
  
``` Kotlin
@Composable
fun InventoryNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = InventoryScreenOptionSelectRoute
    ) {
        /*remaining destinations*/
        composable<ItemDetailsScreenRoute> {
            val args = it.toRoute<ItemDetailsScreenRoute>()
            ItemDetailsScreenCollector(
                args = args,
                navController = navController,
                modifier = modifier
            )
        }
    }
} 
```

</details>

## Demonstration
In order: LoadingScreen, Main Menu, Item Details, Delete Prompt, Item Edit/Insert

<img src="https://github.com/user-attachments/assets/60b2b34e-f9b7-4525-ae52-5bab21e78c7d" width="150" height="280">

<img src="https://github.com/user-attachments/assets/8f166bfa-ad86-4b4e-a326-d3f3af0233ba" width="150" height="280">

<img src="https://github.com/user-attachments/assets/68d21320-91b1-4b88-b11a-fa1d569a1af2" width="150" height="280">

<img src="https://github.com/user-attachments/assets/4b171864-f0b7-4a11-8fe0-d0c6979a56bc" width="150" height="280">

<img src="https://github.com/user-attachments/assets/00889c7f-792e-432d-a245-6b427b711105" width="150" height="280">
