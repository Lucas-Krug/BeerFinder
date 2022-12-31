package de.lucas.beerfinder.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.lucas.beerfinder.model.api.ApiConstant
import de.lucas.beerfinder.model.api.ApiService
import de.lucas.beerfinder.model.database.BeerDb
import de.lucas.beerfinder.model.database.IngredientConverter
import de.lucas.beerfinder.model.database.ListConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

private val json = Json { ignoreUnknownKeys = true }

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiConstant() = ApiConstant(baseUrl = "https://api.punkapi.com/v2/")

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor {
                Timber.tag("http").e(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(apiConstant: ApiConstant, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(apiConstant.baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideBeerDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BeerDb::class.java, "beer_db")
            .addTypeConverter(IngredientConverter())
            .addTypeConverter(ListConverter())
            .build()

    @Singleton
    @Provides
    fun provideBeerDao(db: BeerDb) = db.beerDao()
}