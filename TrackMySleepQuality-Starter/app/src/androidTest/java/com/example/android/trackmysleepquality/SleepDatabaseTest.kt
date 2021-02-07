/*
 * Copyright 2019, The Android Open Source Project
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

package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    /**
     * Executed before the real tests are executed. Here, you create a virtual in-memory database
     * and DAO object. In-memory databases disappear when the app is finished, so it doesn't
     * affects the real database in file system.
     */
    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    /**
     * Executed after the tests are finished. We only need to close the database here. As it is
     * in-memory, all entities are removed from the memory when closed.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Actual test method. Here, we tested a portion of methods, but it is recommended to test
     * all the DAO methods.
     * <b>
     *
     * In a real test, you would have multiple test methods.
     */
    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndClear() {
        val nights = listOf(SleepNight(), SleepNight(), SleepNight())
        nights.forEach { sleepDao.insert(it) }
        sleepDao.clear()
        val nullNight = sleepDao.getTonight()
        assertEquals(nullNight, null)
    }
}