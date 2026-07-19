package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.ScannedFood
import com.example.ui.screens.FoodDetailsContent
import com.example.ui.screens.HomeScreenContent
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class AppScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun home_screen_screenshot() {
        val mockHistory = listOf(
            ScannedFood(
                id = 1,
                name = "Nasi Goreng Spesial",
                confidence = 0.95f,
                imagePath = "", // uses fallback icon
                timestamp = System.currentTimeMillis() - 3600000,
                calories = 350,
                protein = 12,
                fat = 14,
                carbs = 45,
                fiber = 3
            ),
            ScannedFood(
                id = 2,
                name = "Sate Ayam Madura",
                confidence = 0.88f,
                imagePath = "", // uses fallback icon
                timestamp = System.currentTimeMillis() - 7200000,
                calories = 280,
                protein = 22,
                fat = 10,
                carbs = 15,
                fiber = 1
            )
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                HomeScreenContent(
                    history = mockHistory,
                    onCameraClick = {},
                    onGalleryClick = {},
                    onLiveCameraClick = {},
                    onDeleteHistoryItem = {},
                    onClearHistory = {},
                    onHistoryItemClick = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/home_screen.png")
    }

    @Test
    fun result_screen_screenshot() {
        val mockFood = ScannedFood(
            id = 1,
            name = "Nasi Goreng Khas Indonesia",
            confidence = 0.96f,
            imagePath = "", // uses fallback icon
            timestamp = System.currentTimeMillis(),
            calories = 380,
            protein = 14,
            fat = 12,
            carbs = 50,
            fiber = 4,
            hasRecipe = true,
            recipeTitle = "Nasi Goreng Spesial",
            recipeIngredients = "3 piring Nasi putih, 3 siung Bawang putih halus, 5 butir Bawang merah iris, 2 sdm Kecap manis, 1 sdt Garam, 2 butir Telur",
            recipeInstructions = "1. Tumis bawang putih dan bawang merah iris hingga harum.\n2. Masukkan telur, orak-arik hingga matang.\n3. Masukkan nasi putih, aduk hingga rata.\n4. Tambahkan kecap manis dan garam. Masak hingga merata dan matang.\n5. Sajikan selagi hangat."
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                FoodDetailsContent(food = mockFood)
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/result_screen.png")
    }
}
