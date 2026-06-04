
import React, { useEffect, useRef } from 'react';
import * as THREE from 'three';
import { Emotion, EmotionColors } from '../types';

interface SylpfAvatarProps {
  emotion: Emotion;
  isSpeaking: boolean;
  isProcessing: boolean;
}

const SylpfAvatar: React.FC<SylpfAvatarProps> = ({ emotion, isSpeaking, isProcessing }) => {
  const mountRef = useRef<HTMLDivElement>(null);
  const rendererRef = useRef<THREE.WebGLRenderer | null>(null);
  const sceneRef = useRef<THREE.Scene | null>(null);
  const modelRef = useRef<{
    torso: THREE.Mesh;
    head: THREE.Mesh;
    neck: THREE.Mesh;
    shoulderLeft: THREE.Mesh;
    elbowLeft: THREE.Mesh;
    shoulderRight: THREE.Mesh;
    elbowRight: THREE.Mesh;
  } | null>(null);

  useEffect(() => {
    if (!mountRef.current) return;

    const width = 240;
    const height = 320;

    // Scene setup
    const scene = new THREE.Scene();
    sceneRef.current = scene;

    const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
    camera.position.z = 40;

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(width, height);
    rendererRef.current = renderer;
    mountRef.current.appendChild(renderer.domElement);

    // Lights
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1.2);
    directionalLight.position.set(5, 5, 5);
    scene.add(directionalLight);

    // Materials
    const skinMaterial = new THREE.MeshStandardMaterial({ 
      color: 0xe0e0e0,
      roughness: 0.3, 
      metalness: 0.4 
    });

    const clothingMaterial = new THREE.MeshStandardMaterial({ 
      color: EmotionColors[emotion], 
      roughness: 0.7, 
      metalness: 0.1 
    });

    // Model Construction (Hierarchical for better animation)
    const torsoGroup = new THREE.Group();
    scene.add(torsoGroup);

    const torsoGeometry = new THREE.CylinderGeometry(5, 4, 18, 32);
    const torso = new THREE.Mesh(torsoGeometry, clothingMaterial);
    torsoGroup.add(torso);

    const neckGeometry = new THREE.CylinderGeometry(1.5, 2, 4, 32);
    const neck = new THREE.Mesh(neckGeometry, skinMaterial);
    neck.position.y = 11;
    torsoGroup.add(neck);

    const headGeometry = new THREE.SphereGeometry(5.5, 32, 32);
    const head = new THREE.Mesh(headGeometry, skinMaterial);
    head.position.y = 15;
    torsoGroup.add(head);

    // Arms
    const shoulderLeftGeometry = new THREE.CylinderGeometry(1.5, 1.5, 8, 32);
    const shoulderLeft = new THREE.Mesh(shoulderLeftGeometry, skinMaterial);
    shoulderLeft.position.set(-6, 7, 0);
    torsoGroup.add(shoulderLeft);

    const elbowLeftGeometry = new THREE.CylinderGeometry(1.2, 1.2, 8, 32);
    const elbowLeft = new THREE.Mesh(elbowLeftGeometry, skinMaterial);
    elbowLeft.position.set(-9, 0, 0);
    shoulderLeft.add(elbowLeft);

    const shoulderRightGeometry = new THREE.CylinderGeometry(1.5, 1.5, 8, 32);
    const shoulderRight = new THREE.Mesh(shoulderRightGeometry, skinMaterial);
    shoulderRight.position.set(6, 7, 0);
    torsoGroup.add(shoulderRight);

    const elbowRightGeometry = new THREE.CylinderGeometry(1.2, 1.2, 8, 32);
    const elbowRight = new THREE.Mesh(elbowRightGeometry, skinMaterial);
    elbowRight.position.set(9, 0, 0);
    shoulderRight.add(elbowRight);

    modelRef.current = { torso, head, neck, shoulderLeft, elbowLeft, shoulderRight, elbowRight };

    let animationFrameId: number;
    let time = 0;

    const animate = () => {
      animationFrameId = requestAnimationFrame(animate);
      time += 0.02;

      if (modelRef.current) {
        const { torso, head, shoulderLeft, shoulderRight, elbowLeft, elbowRight } = modelRef.current;
        
        // Amplitude and Speed factors based on Emotion
        let amplitude = 1;
        let breathSpeed = 2;
        
        if (emotion === Emotion.EXCITED || emotion === Emotion.HAPPY) {
          amplitude = 1.5;
          breathSpeed = 4;
        } else if (emotion === Emotion.SAD || emotion === Emotion.SLEEPING) {
          amplitude = 0.4;
          breathSpeed = 0.8;
        } else if (emotion === Emotion.INTENSE || emotion === Emotion.DEFENSIVE) {
          amplitude = 1.2;
          breathSpeed = 3;
        }

        // Breathing & Idle Movement
        torso.scale.y = 1 + Math.sin(time * breathSpeed) * 0.03 * amplitude;
        torso.rotation.y = Math.sin(time * breathSpeed / 2) * 0.05 * amplitude;
        
        head.rotation.y = Math.sin(time * 0.5) * 0.15 * amplitude;
        head.rotation.x = Math.cos(time * 0.3) * 0.08 * amplitude;
        
        // Arm Sway
        shoulderLeft.rotation.z = Math.sin(time * 1.5) * 0.2 * amplitude - 0.2;
        shoulderRight.rotation.z = Math.sin(time * 1.5 + Math.PI) * 0.2 * amplitude + 0.2;
        
        elbowLeft.rotation.z = Math.sin(time * 2) * 0.15 * amplitude;
        elbowRight.rotation.z = Math.sin(time * 2 + Math.PI / 2) * 0.15 * amplitude;

        // Interaction Effects
        if (isSpeaking || isProcessing) {
          head.rotation.x += Math.sin(time * 12) * 0.04;
          shoulderLeft.rotation.z += Math.sin(time * 8) * 0.15;
          shoulderRight.rotation.z += Math.cos(time * 8) * 0.15;
          torso.position.y = Math.sin(time * 10) * 0.2;
        } else {
          torso.position.y = 0;
        }

        // Sleeping state
        if (emotion === Emotion.SLEEPING) {
           head.rotation.x = 0.4;
           torso.rotation.x = 0.1;
        } else {
           torso.rotation.x = 0;
        }

        // Update clothing color based on emotion
        clothingMaterial.color.set(EmotionColors[emotion]);
      }

      renderer.render(scene, camera);
    };

    animate();

    return () => {
      cancelAnimationFrame(animationFrameId);
      if (mountRef.current && rendererRef.current) {
        mountRef.current.removeChild(rendererRef.current.domElement);
      }
      renderer.dispose();
    };
  }, [emotion, isSpeaking, isProcessing]);

  return (
    <div className="relative group">
      {/* Aura background */}
      <div 
        className="avatar-aura" 
        style={{ borderColor: EmotionColors[emotion] }}
      />
      <div ref={mountRef} className="z-10" />
      
      {/* HUD Info */}
      <div className="absolute -bottom-4 left-1/2 -translate-x-1/2 flex flex-col items-center">
         <div className="px-3 py-1 bg-black/80 backdrop-blur-md border border-white/10 rounded-full">
            <span className="text-[10px] font-bold tracking-[0.2em] text-white uppercase" style={{ color: EmotionColors[emotion] }}>
              {emotion}
            </span>
         </div>
      </div>
    </div>
  );
};

export default SylpfAvatar;
