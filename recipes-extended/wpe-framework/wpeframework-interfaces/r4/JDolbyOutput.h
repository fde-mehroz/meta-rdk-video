
// Generated automatically from 'IDolby.h'. DO NOT EDIT.

#pragma once

#include "Module.h"
#include "JsonData_DolbyOutput.h"
#include <interfaces/IDolby.h>

namespace WPEFramework {

namespace Exchange {

    namespace Dolby {

        namespace JOutput {

            namespace Version {

                constexpr uint8_t Major = 1;
                constexpr uint8_t Minor = 0;
                constexpr uint8_t Patch = 0;

            } // namespace Version

            using namespace JsonData::Dolby::Output;

            static void Register(PluginHost::JSONRPC& _module, IOutput* _destination)
            {
                ASSERT(_destination != nullptr);

                _module.RegisterVersion(_T("JOutput"), Version::Major, Version::Minor, Version::Patch);

                // Property: atmosmetadata - Atmos capabilities of Sink (r/o)
                _module.Register<void, Core::JSON::Boolean>(_T("dolby_atmosmetadata"),
                    [_destination](Core::JSON::Boolean& Result) -> uint32_t {
                        uint32_t _errorCode;
                        // read-only property get
                        bool _result{};
                        _errorCode = _destination->AtmosMetadata(_result);
                        if (_errorCode == Core::ERROR_NONE) {
                            Result = _result;
                        }
                        return (_errorCode);
                    });

                // Property: soundmode - Sound Mode - Mono/Stereo/Surround (r/o)
                _module.Register<void, Core::JSON::EnumType<Exchange::Dolby::IOutput::SoundModes>>(_T("dolby_soundmode"),
                    [_destination](Core::JSON::EnumType<Exchange::Dolby::IOutput::SoundModes>& Result) -> uint32_t {
                        uint32_t _errorCode;
                        // read-only property get
                        Exchange::Dolby::IOutput::SoundModes _result{};
                        _errorCode = _destination->SoundMode(_result);
                        if (_errorCode == Core::ERROR_NONE) {
                            Result = _result;
                        }
                        return (_errorCode);
                    });

                // Property: enableatmosoutput - Enable Atmos Audio Output (w/o)
                _module.Register<Core::JSON::Boolean, void>(_T("dolby_enableatmosoutput"),
                    [_destination](const Core::JSON::Boolean& Params) -> uint32_t {
                        uint32_t _errorCode;
                        // write-only property set
                        const bool _params{Params};
                        _errorCode = _destination->EnableAtmosOutput(_params);
                        return (_errorCode);
                    });

                // Property: mode - Dolby Mode
                _module.Register<Core::JSON::EnumType<Exchange::Dolby::IOutput::Type>, Core::JSON::EnumType<Exchange::Dolby::IOutput::Type>>(_T("dolby_mode"),
                    [_destination](const Core::JSON::EnumType<Exchange::Dolby::IOutput::Type>& Params, Core::JSON::EnumType<Exchange::Dolby::IOutput::Type>& Result) -> uint32_t {
                        uint32_t _errorCode;
                        if (Params.IsSet() == false) {
                            // property get
                            Exchange::Dolby::IOutput::Type _result{};
                            _errorCode = (static_cast<const IOutput*>(_destination))->Mode(_result);
                            if (_errorCode == Core::ERROR_NONE) {
                                Result = _result;
                            }
                        } else {
                            // property set
                            const Exchange::Dolby::IOutput::Type _params{Params};
                            _errorCode = _destination->Mode(_params);
                            Result.Null(true);
                        }
                        return (_errorCode);
                    });

            }

            static void Unregister(PluginHost::JSONRPC& _module)
            {
                _module.Unregister(_T("dolby_atmosmetadata"));
                _module.Unregister(_T("dolby_soundmode"));
                _module.Unregister(_T("dolby_enableatmosoutput"));
                _module.Unregister(_T("dolby_mode"));
            }

            namespace Event {

                // Event: audiomodechanged
                static void AudioModeChanged(const PluginHost::JSONRPC& _module, const Exchange::Dolby::IOutput::SoundModes& mode, const bool& enabled)
                {
                    AudioModeChangedParamsData _params;
                    _params.Mode = static_cast<Exchange::Dolby::IOutput::SoundModes>(mode);
                    _params.Enabled = enabled;

                    _module.Notify(_T("dolby_audiomodechanged"), _params);
                }

            }; // namespace Event

        }; // namespace JOutput

    } // namespace Dolby

} // namespace Exchange

}

