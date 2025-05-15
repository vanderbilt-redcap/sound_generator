package io.github.mertguner.sound_generator;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.github.mertguner.sound_generator.handlers.getOneCycleDataHandler;
import io.github.mertguner.sound_generator.handlers.isPlayingStreamHandler;
import io.github.mertguner.sound_generator.models.WaveTypes;

/** SoundGeneratorPlugin */
public class SoundGeneratorPlugin implements FlutterPlugin, MethodCallHandler {
  private SoundGenerator soundGenerator = new SoundGenerator();
  private MethodChannel channel;
  private EventChannel isPlayingEventChannel;
  private EventChannel oneCycleDataEventChannel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sound_generator");
    channel.setMethodCallHandler(this);

    isPlayingEventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), isPlayingStreamHandler.NATIVE_CHANNEL_EVENT);
    isPlayingEventChannel.setStreamHandler(new isPlayingStreamHandler());

    oneCycleDataEventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), getOneCycleDataHandler.NATIVE_CHANNEL_EVENT);
    oneCycleDataEventChannel.setStreamHandler(new getOneCycleDataHandler());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "init":
        int sampleRate = call.argument("sampleRate");
        result.success(soundGenerator.init(sampleRate));
        break;
      case "release":
        soundGenerator.release();
        result.success(null);
        break;
      case "play":
        soundGenerator.startPlayback();
        result.success(null);
        break;
      case "stop":
        soundGenerator.stopPlayback();
        result.success(null);
        break;
      case "isPlaying":
        result.success(soundGenerator.isPlaying());
        break;
      case "setAutoUpdateOneCycleSample":
        boolean autoUpdate = call.argument("autoUpdateOneCycleSample");
        soundGenerator.setAutoUpdateOneCycleSample(autoUpdate);
        result.success(null);
        break;
      case "setFrequency":
        double frequency = call.argument("frequency");
        soundGenerator.setFrequency((float) frequency);
        result.success(null);
        break;
      case "setWaveform":
        String waveType = call.argument("waveType");
        soundGenerator.setWaveform(WaveTypes.valueOf(waveType));
        result.success(null);
        break;
      case "setBalance":
        double balance = call.argument("balance");
        soundGenerator.setBalance((float) balance);
        result.success(null);
        break;
      case "setVolume":
        double volume = call.argument("volume");
        soundGenerator.setVolume((float) volume);
        result.success(null);
        break;
      case "getSampleRate":
        result.success(soundGenerator.getSampleRate());
        break;
      case "refreshOneCycleData":
        soundGenerator.refreshOneCycleData();
        result.success(null);
        break;
      case "getAmplitude":
        result.success(soundGenerator.getAmplitude());
        break;
      case "setAmplitude":
        double amplitude = call.argument("amplitude");
        soundGenerator.setAmplitude((float) amplitude);
        result.success(null);
        break;
      default:
        result.notImplemented();
        break;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
    if (isPlayingEventChannel != null) {
      isPlayingEventChannel.setStreamHandler(null);
      isPlayingEventChannel = null;
    }
    if (oneCycleDataEventChannel != null) {
      oneCycleDataEventChannel.setStreamHandler(null);
      oneCycleDataEventChannel = null;
    }
  }
}
