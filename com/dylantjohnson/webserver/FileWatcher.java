package com.dylantjohnson.webserver;

import java.io.*;
import java.security.*;
import java.util.*;

/**
 * A class that watches a file for changes and notifies any observers.
 * <p>
 * This class will periodically compute an MD5 hash of the file it's watching and see if it
 * changed.
 * <p>
 * This class is thread-safe.
 */
class FileWatcher {
    private final int FILE_POLL_RATE_SEC = 10;

    private Thread mThread;
    private File mFile;
    private List<Runnable> mListeners;
    private byte[] mHash;

    /**
     * Create a new FileWatcher with a specific file.
     *
     * @param file the file to watch
     * @throws FileHashingException if unable to watch the file
     */
    public FileWatcher(File file) throws FileHashingException {
        mFile = file;
        mListeners = new ArrayList<>();
        mHash = hash(file);
    }

    /**
     * Begin watching the configured file for changes.
     */
    public synchronized void start() {
        if (mThread == null) {
            System.out.println(String.format("Watching %s for changes...", mFile));
            mThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(FILE_POLL_RATE_SEC * 1000L);
                        checkFile();
                    } catch (FileHashingException ex) {
                        continue;
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            });
            mThread.start();
        }
    }

    /**
     * Stop watching the configured file for changes.
     */
    public synchronized void stop() {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    /**
     * Hash the configured file and check if it's changed.
     *
     * @throws FileHashingException if unable to hash the file
     */
    synchronized void checkFile() throws FileHashingException {
        var newHash = hash(mFile);
        if (!Arrays.equals(mHash, newHash)) {
            mHash = newHash;
            for (var listener : mListeners) {
                listener.run();
            }
        }
    }

    /**
     * Add an observer that will be notified when the configured file changes.
     *
     * @param listener the observer to add
     */
    public synchronized void addListener(Runnable listener) {
        mListeners.add(listener);
    }

    /**
     * Calculate the MD5 hash of a file.
     *
     * @param file the file to hash
     * @return the hash as a byte array
     * @throws FileHashingException if unable to hash the file
     */
    private static byte[] hash(File file) throws FileHashingException {
        MessageDigest hasher;
        try  {
            hasher = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            throw new FileHashingException(ex);
        }
        try (var fileStream = new FileInputStream(file);
                var reader = new DigestInputStream(fileStream, hasher)) {
            int b = reader.read();
            while (b > -1) {
                b = reader.read();
            }
            return hasher.digest();
        } catch (Exception ex) {
            throw new FileHashingException(ex);
        }
    }
}
